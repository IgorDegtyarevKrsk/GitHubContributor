package com.degtyarev.githubcontributor.data.di

import dagger.Component

@Component(
    dependencies = [ContextProvider::class],
    modules = [DataModule::class]
)
interface DataComponent {
    @Component.Factory
    interface Factory {
        fun create(contextProvider: ContextProvider): DataComponent
    }
}