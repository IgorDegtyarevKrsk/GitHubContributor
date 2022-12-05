package com.githubcontributor.app.di

import com.degtyarev.githubcontributor.data.di.ContextProvider
import com.degtyarev.githubcontributor.data.di.DaggerDataComponent
import com.degtyarev.githubcontributor.data.di.DataComponent
import com.degtyarev.githubcontributor.data.di.DataModule
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
        DataComponent::class
                   ],
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

    companion object {
        fun init(app: App): AppComponent {

            val contextModule = ContextModule(app)
            val contextProvider: ContextProvider = DaggerContextComponent.builder()
                .contextModule(contextModule)
                .build()

            val dataComponent = DaggerDataComponent
                .factory()
                .create(contextProvider)

            return DaggerAppComponent.builder()
                .contextModule(contextModule)
                .dataComponent(dataComponent)
                .build()
        }
    }

}