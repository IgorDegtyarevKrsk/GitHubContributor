package com.degtyarev.githubcontributor.data.di.module

import android.content.Context
import com.degtyarev.githubcontributor.data.repository.UserRepositoryImpl
import com.degtyarev.githubcontributor.data.storage.UserDataSource
import com.degtyarev.githubcontributor.data.storage.prefs.UserDataSourceImpl
import com.githubcontributor.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferencesModule {
    @Singleton
    @Provides
    fun provideUserDataSource(context: Context): UserDataSource =
        UserDataSourceImpl(context)

    @Singleton
    @Provides
    fun provideUserRepository(userDataSource: UserDataSource): UserRepository =
        UserRepositoryImpl(userDataSource)
}