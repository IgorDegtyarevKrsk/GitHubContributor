package com.githubcontributor.presentation

import android.app.Application
import com.githubcontributor.presentation.di.AppComponent
import com.githubcontributor.presentation.di.ContextModule
import com.githubcontributor.presentation.di.DaggerAppComponent

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}