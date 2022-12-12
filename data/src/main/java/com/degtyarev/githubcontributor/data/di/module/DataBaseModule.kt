package com.degtyarev.githubcontributor.data.di.module

import android.content.Context
import androidx.room.Room
import com.degtyarev.githubcontributor.data.repository.GitHubUserLocalRepositoryImpl
import com.degtyarev.githubcontributor.data.storage.GitHubUserLocalDataSource
import com.degtyarev.githubcontributor.data.storage.db.AppDatabase
import com.degtyarev.githubcontributor.data.storage.db.GitHubUserLocalDataSourceImpl
import com.githubcontributor.domain.repository.GitHubUserLocalRepository
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule {

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
}