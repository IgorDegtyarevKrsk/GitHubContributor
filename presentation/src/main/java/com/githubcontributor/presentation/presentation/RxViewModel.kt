package com.githubcontributor.presentation.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.DisposableContainer

abstract class RxViewModel: ViewModel() {
    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }
    val disposableContainer: DisposableContainer = disposable

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}