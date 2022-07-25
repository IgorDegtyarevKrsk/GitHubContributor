package com.githubcontributor.presentation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.githubcontributor.domain.repository.GitHubUserLocalRepository
import com.githubcontributor.domain.repository.UserRepository
import com.githubcontributor.domain.usecase.GetSavedVariant
import com.githubcontributor.domain.usecase.GetTokenUseCase
import com.githubcontributor.domain.usecase.SaveParamsUseCase
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