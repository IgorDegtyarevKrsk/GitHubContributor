package com.githubcontributor.app.di

import com.degtyarev.githubcontributor.data.di.*
import com.githubcontributor.app.App
import com.githubcontributor.app.di.context.ContextModule
import com.githubcontributor.app.di.context.DaggerContextComponent
import com.githubcontributor.domain.repository.GitHubRepository
import com.githubcontributor.presentation.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        DataProvider::class
                   ],
    modules = [
        ViewModelModule::class
    ]
)
interface AppComponent {

    fun createGitHubRepository(): GitHubRepository

    fun inject(mainActivity: MainActivity)

    companion object {
        fun init(app: App): AppComponent {

            val contextModule = ContextModule(app)
            val contextProvider: ContextProvider = DaggerContextComponent.builder()
                .contextModule(contextModule)
                .build()

            val dataProvider = DaggerDataComponent
                .factory()
                .create(contextProvider)

            return DaggerAppComponent.builder()
                .dataProvider(dataProvider)
                .build()
        }
    }

}