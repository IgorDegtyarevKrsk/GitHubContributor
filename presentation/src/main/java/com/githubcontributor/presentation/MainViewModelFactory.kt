package com.githubcontributor.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.githubcontributor.presentation.ui.MainViewModel
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
//    private val userRepository: UserRepository,
//    private val gitHubUserLocalRepository: GitHubUserLocalRepository
    private val mainViewModel: MainViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return MainViewModel(
//            GetTokenUseCase(userRepository),
//            GetSavedVariant(userRepository),
//            SaveParamsUseCase(userRepository),
//            gitHubUserLocalRepository
//        ) as T
        return mainViewModel as T
    }
}