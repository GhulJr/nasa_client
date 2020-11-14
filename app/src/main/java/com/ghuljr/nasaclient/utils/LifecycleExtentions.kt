package com.ghuljr.nasaclient.utils

import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.ghuljr.nasaclient.ui.base.mvp.MVPLifecycleObserver

fun <VIEW : BaseView<PRESENTER>, PRESENTER : BasePresenter<VIEW>> VIEW.getLifecycleObserver()
        : MVPLifecycleObserver<VIEW, PRESENTER> {
    return MVPLifecycleObserver(this, getPresenter(), getState())
}


