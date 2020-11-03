package com.ghuljr.nasaclient.ui.base.mvp

import androidx.annotation.CallSuper

abstract class BasePresenter<VIEW>: PresenterContract<VIEW> {

    protected var view: VIEW? = null
    protected var retainedState: RetainedState? = null

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
    override fun onViewAttached() { /* Do nothing*/ }

    @CallSuper
    override fun onViewDetached() { /* Do nothing*/ }
}

interface PresenterContract<VIEW> {
    fun attach(view: VIEW, retainedState: RetainedState)
    fun detach()
    fun onViewAttached()
    fun onViewDetached()
}
