package com.ghuljr.nasaclient.ui.base.mvp

interface BaseView<PRESENTER> {
    fun getPresenter(): PRESENTER
    fun getState(): RetainedState
}