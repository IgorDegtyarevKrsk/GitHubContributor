package com.githubcontributor.presentation.di

import com.degtyarev.githubcontributor.data.storage.GitHubService
import com.degtyarev.githubcontributor.data.storage.api.createGitHubService
import com.githubcontributor.domain.repository.UserRepository
import dagger.Module
import dagger.Provides

@Module
class GitHubModule {

    @Provides
    fun provideGitHubService(userRepository: UserRepository): GitHubService {
        val userName = userRepository.userName
        val token = userRepository.token
        return createGitHubService(userName, token)
    }

}