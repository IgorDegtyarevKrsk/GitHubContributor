package com.githubcontributor.app.di

import androidx.lifecycle.ViewModelProvider
import com.githubcontributor.presentation.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    fun provideViewModelFactory(factory: MainViewModelFactory): ViewModelProvider.Factory = factory

}