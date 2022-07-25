package com.githubcontributor.presentation.di

import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.presentation.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextModule::class,
        DataModule::class,
        ViewModelModule::class,
        GitHubModule::class
    ]
)
interface AppComponent {

    fun createGitHubRepository(): GitHubRepository

    fun inject(mainActivity: MainActivity)

}