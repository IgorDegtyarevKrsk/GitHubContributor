package com.githubcontributor.presentation.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.githubcontributor.domain.RequestData
import com.githubcontributor.domain.RequestVariant
import com.githubcontributor.domain.User
import com.githubcontributor.domain.Variant
import com.githubcontributor.domain.repository.GitHubUserLocalRepository
import com.githubcontributor.domain.tasks.*
import com.githubcontributor.domain.tasks.rx.RequestRxProgress
import com.githubcontributor.domain.tasks.rx.RequestRxSequence
import com.githubcontributor.domain.usecase.GetSavedVariant
import com.githubcontributor.domain.usecase.GetTokenUseCase
import com.githubcontributor.domain.usecase.SaveParamsUseCase
import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.presentation.RxViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getTokenUseCase: GetTokenUseCase,
    private val getSavedVariant: GetSavedVariant,
    private val saveParamsUseCase: SaveParamsUseCase,
    private val gitHubUserLocalRepository: GitHubUserLocalRepository,
    private val requestRxSequence: RequestRxSequence,
    private val concurrentRxProgress: RequestRxProgress
): RxViewModel() {

    private val _accessTokenQuestionEnabled = MutableLiveData(
        getTokenUseCase.getToken().isEmpty()
    )
    val accessTokenQuestionEnabled: LiveData<Boolean>
        get() = _accessTokenQuestionEnabled

    val savedVariant: RequestVariant by lazy { getSavedVariant.get() }

    private val _loadButtonEnabled = MutableLiveData<Boolean>()
    val loadButtonEnabled: LiveData<Boolean>
        get() = _loadButtonEnabled

    private val _cancelButtonEnabled = MutableLiveData<Boolean>()
    val cancelButtonEnabled: LiveData<Boolean>
        get() = _cancelButtonEnabled

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private val _iconRunning = MutableLiveData(false)
    val iconRunning: LiveData<Boolean>
        get() = _iconRunning

    private val _loadingStatusText = MutableLiveData("")
    val loadingStatusText: LiveData<String>
        get() = _loadingStatusText

    init {
        viewModelScope.launch {
            val users = gitHubUserLocalRepository.getUsers()
            _users.postValue(users)
        }
    }

    fun chooseVariant(req: RequestData, variant: Variant, gitHubRepositoryProvider: () -> GitHubRepository) {
        saveParamsUseCase.save(req, variant)
        _accessTokenQuestionEnabled.value = req.password.isEmpty()

        clearResults()
        val gitHubRepository = gitHubRepositoryProvider.invoke()

        val startTime = System.currentTimeMillis()
        _iconRunning.value = true
        _loadingStatusText.value = "In progress"
        when (variant) {
            Variant.BACKGROUND -> {
                loadContributorsBackground(gitHubRepository, req) { users ->
                    Handler(Looper.getMainLooper()).post {
                        updateResults(users, startTime)
                    }
                }
            }
            Variant.CALLBACKS -> {
                loadContributorsCallbacks(gitHubRepository, req) { users ->
                    updateResults(users, startTime)
                }
            }
            Variant.SUSPEND -> {
                viewModelScope.launch {
                    val users = loadContributorsSuspend(gitHubRepository, req)
                    updateResults(users, startTime)
                }.setUpCancellation()
            }
            Variant.CONCURRENT -> {
                viewModelScope.launch {
                    val users = loadContributorsConcurrent(gitHubRepository, req)
                    updateResults(users, startTime)
                }.setUpCancellation()
            }
            Variant.NOT_CANCELLABLE -> {
                viewModelScope.launch {
                    val users = loadContributorsNotCancellable(gitHubRepository, req)
                    updateResults(users, startTime)
                }.setUpCancellation()
            }
            Variant.PROGRESS -> {
                viewModelScope.launch(Dispatchers.Default) {
                    loadContributorsProgress(gitHubRepository, req) { users, completed ->
                        withContext(Dispatchers.Main) {
                            updateResults(users, startTime, completed)
                        }
                    }
                }.setUpCancellation()
            }
            Variant.CHANNELS -> { // Performing request concurrently and showing progress
                viewModelScope.launch(Dispatchers.Default) {
                    loadContributorsChannels(gitHubRepository, req) { users, completed ->
                        withContext(Dispatchers.Main) {
                            updateResults(users, startTime, completed)
                        }
                    }
                }.setUpCancellation()
            }
            Variant.Rx -> {
                disposableContainer.add(
                    requestRxSequence.loadContributorsRxSequence(req)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { users -> updateResults(users, startTime) }
                        .setUpCancellation()
                )
            }
            Variant.RxProgress -> {
                disposableContainer.add(
                    concurrentRxProgress.loadContributorsRxProgress(req)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            updateLoadingStatus(LoadingStatus.COMPLETED, startTime)
                            setActionStatus(newLoadingEnabled = true)
                        }
                        .subscribe { users -> updateResults(users, startTime, false) }
                        .setUpCancellation()
                )
            }
        }
    }

    private fun clearResults() {
        viewModelScope.launch {
            gitHubUserLocalRepository.clear()
            _users.postValue(emptyList())
        }
        updateLoadingStatus(LoadingStatus.IN_PROGRESS)
        setActionStatus(newLoadingEnabled = false)
    }

    private enum class LoadingStatus { COMPLETED, CANCELED, IN_PROGRESS }

    private fun updateResults(
        users: List<User>,
        startTime: Long,
        completed: Boolean = true
    ) {
        viewModelScope.launch {
            gitHubUserLocalRepository.saveAll(users)
            _users.postValue(users)
        }
        updateLoadingStatus(if (completed) LoadingStatus.COMPLETED else LoadingStatus.IN_PROGRESS, startTime)
        if (completed) {
            setActionStatus(newLoadingEnabled = true)
        }
    }

    private fun updateLoadingStatus(
        status: LoadingStatus,
        startTime: Long? = null
    ) {
        val time = if (startTime != null) {
            val time = System.currentTimeMillis() - startTime
            "${time / 1000}.${time % 1000 / 100} sec"
        } else ""

        val text = "Loading status: " +
                when (status) {
                    LoadingStatus.COMPLETED -> "completed in $time"
                    LoadingStatus.IN_PROGRESS -> "In progress $time"
                    LoadingStatus.CANCELED -> "canceled"
                }
        _loadingStatusText.value = text
        _iconRunning.value = status == LoadingStatus.IN_PROGRESS
    }

    private var loadingJob: Job? = null
    private var loadingDisposable: Disposable? = null

    fun cancelClicked() {
        loadingJob?.cancel()
        loadingDisposable?.dispose()
        setActionStatus(newLoadingEnabled = true)
        updateLoadingStatus(LoadingStatus.CANCELED)
    }

    private fun Job.setUpCancellation() {
        // make active the 'cancel' button
        setActionStatus(newLoadingEnabled = false, cancellationEnabled = true)

        loadingJob = this

        // cancel the loading job if the 'cancel' button was clicked
        viewModelScope.launch {
            loadingJob!!.join()
            setActionStatus(newLoadingEnabled = true)
        }
    }

    private fun Disposable.setUpCancellation(): Disposable {
        setActionStatus(newLoadingEnabled = false, cancellationEnabled = true)
        loadingDisposable = this

        return this
    }

    private fun setActionStatus(newLoadingEnabled: Boolean, cancellationEnabled: Boolean = false) {
        _loadButtonEnabled.value = newLoadingEnabled
        _cancelButtonEnabled.value = cancellationEnabled
    }

}