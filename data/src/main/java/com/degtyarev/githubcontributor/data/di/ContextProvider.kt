package com.degtyarev.githubcontributor.data.di

import android.content.Context

interface ContextProvider {
    fun provideContext(): Context
}