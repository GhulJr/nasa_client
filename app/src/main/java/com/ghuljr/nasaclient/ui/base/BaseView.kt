package com.ghuljr.nasaclient.ui.base

interface BaseView<PRESENTER> {
    fun getPresenter(): PRESENTER
}