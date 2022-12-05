package com.githubcontributor.app

import android.app.Application
import com.githubcontributor.presentation.PresentationProvider
import com.githubcontributor.app.di.AppComponent
import com.githubcontributor.app.di.context.ContextModule
import com.githubcontributor.app.di.DaggerAppComponent
import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.presentation.ui.MainActivity

class App: Application(), PresentationProvider {

    override fun onCreate() {
        super.onCreate()
        appComponent = AppComponent.init(this)
    }

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun getGithubRepository(): GitHubRepository {
        return appComponent.createGitHubRepository()
    }

    override fun inject(mainActivity: MainActivity) {
        appComponent.inject(mainActivity)
    }
}