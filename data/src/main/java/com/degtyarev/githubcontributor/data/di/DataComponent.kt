package com.degtyarev.githubcontributor.data.di

import com.degtyarev.githubcontributor.data.di.module.DataBaseModule
import com.degtyarev.githubcontributor.data.di.module.GitHubModule
import com.degtyarev.githubcontributor.data.di.module.SharedPreferencesModule
import com.degtyarev.githubcontributor.data.storage.GitHubUserLocalDataSource
import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.repository.GitHubUserLocalRepository
import com.githubcontributor.domain.repository.UserRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [ContextProvider::class],
    modules = [
        SharedPreferencesModule::class,
        DataBaseModule::class,
        GitHubModule::class
    ]
)
interface DataComponent: DataProvider {
    @Component.Factory
    interface Factory {
        fun create(contextProvider: ContextProvider): DataComponent
    }
}

interface DataProvider {
    fun provideUserRepository(): UserRepository
    fun provideGitHubRepository(): GitHubRepository
    fun provideGitHubUserLocalDataSource(): GitHubUserLocalDataSource
    fun provideGitHubUserLocalRepository(): GitHubUserLocalRepository
}