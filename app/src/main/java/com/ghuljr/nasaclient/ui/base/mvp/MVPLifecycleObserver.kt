package com.ghuljr.nasaclient.ui.base.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MVPLifecycleObserver<VIEW : BaseView<PRESENTER>, PRESENTER : BasePresenter<VIEW>>(
    private val view: VIEW,
    private val presenter: PRESENTER,
    private val retainedState: RetainedState
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        presenter.attach(view, retainedState)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        presenter.detach()
    }

}