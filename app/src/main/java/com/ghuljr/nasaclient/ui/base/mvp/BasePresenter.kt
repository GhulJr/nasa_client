package com.ghuljr.nasaclient.ui.base.mvp

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.SerialDisposable

abstract class BasePresenter<VIEW>: PresenterContract<VIEW> {

    protected var view: VIEW? = null
    protected var retainedState: RetainedState? = null
    protected  var disposable: SerialDisposable = SerialDisposable()

    override fun attach(view: VIEW, retainedState: RetainedState) {
        this.view = view
        this.retainedState = retainedState
        onViewAttached()
    }

    override fun detach() {
        view = null
        retainedState = null
        onViewDetached()
    }

    @CallSuper
    override fun onViewAttached() { disposable.set(CompositeDisposable()) }

    @CallSuper
    override fun onViewDetached() { disposable.set(CompositeDisposable()) }
}

interface PresenterContract<VIEW> {
    fun attach(view: VIEW, retainedState: RetainedState)
    fun detach()
    fun onViewAttached()
    fun onViewDetached()
}
