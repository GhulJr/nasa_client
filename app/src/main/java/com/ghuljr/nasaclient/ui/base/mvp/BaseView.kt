package com.ghuljr.nasaclient.ui.base.mvp

import io.reactivex.disposables.CompositeDisposable

interface BaseView<PRESENTER> {
    fun getPresenter(): PRESENTER
    fun getState(): RetainedState
}