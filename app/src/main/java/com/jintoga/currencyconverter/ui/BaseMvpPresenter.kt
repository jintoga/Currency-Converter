package com.jintoga.currencyconverter.ui

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseMvpPresenter<V : MvpView> : MvpBasePresenter<V>() {

    private val disposablesToDisposeOnDetachView = CompositeDisposable()

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        if (!retainInstance) {
            // Dispose all subscriptions that need to be disposed in this lifecycle state.
            disposablesToDisposeOnDetachView.clear()
        }
    }

    protected fun disposeOnDetachView(disposable: Disposable?,
                                      vararg disposables: Disposable) {
        if (disposable != null) {
            disposablesToDisposeOnDetachView.add(disposable)
        }
        for (item in disposables) {
            disposablesToDisposeOnDetachView.add(item)
        }
    }
}