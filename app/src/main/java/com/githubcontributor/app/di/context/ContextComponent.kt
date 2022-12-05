package com.githubcontributor.app.di.context

import com.degtyarev.githubcontributor.data.di.ContextProvider
import dagger.Component

@Component(
    modules = [ContextModule::class]
)
interface ContextComponent: ContextProvider