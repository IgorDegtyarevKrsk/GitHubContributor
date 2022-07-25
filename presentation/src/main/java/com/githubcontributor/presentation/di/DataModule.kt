package com.githubcontributor.presentation.di

import android.content.Context
import androidx.room.Room
import com.degtyarev.githubcontributor.data.repository.GitHubRepositoryImpl
import com.degtyarev.githubcontributor.data.repository.GitHubUserLocalRepositoryImpl
import com.degtyarev.githubcontributor.data.repository.UserRepositoryImpl
import com.degtyarev.githubcontributor.data.storage.GitHubService
import com.degtyarev.githubcontributor.data.storage.GitHubUserLocalDataSource
import com.degtyarev.githubcontributor.data.storage.UserDataSource
import com.degtyarev.githubcontributor.data.storage.db.AppDatabase
import com.degtyarev.githubcontributor.data.storage.db.GitHubUserLocalDataSourceImpl
import com.degtyarev.githubcontributor.data.storage.prefs.UserDataSourceImpl
import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.domain.repository.GitHubUserLocalRepository
import com.githubcontributor.domain.repository.UserRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideUserDataSource(context: Context): UserDataSource =
        UserDataSourceImpl(context)

    @Provides
    fun provideUserRepository(userDataSource: UserDataSource): UserRepository =
        UserRepositoryImpl(userDataSource)

    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database-github"
        ).build()
    }

    @Provides
    fun provideGitHubUserLocalDataSource(appDatabase: AppDatabase): GitHubUserLocalDataSource =
        GitHubUserLocalDataSourceImpl(appDatabase.userDao())

    @Provides
    fun provideGitHubUserLocalRepository(gitHubUserLocalDataSource: GitHubUserLocalDataSource): GitHubUserLocalRepository =
        GitHubUserLocalRepositoryImpl(gitHubUserLocalDataSource)

    @Provides
    fun provideGitHubRepository(gitHubService: GitHubService): GitHubRepository = GitHubRepositoryImpl(gitHubService)
}