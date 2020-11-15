package com.ghuljr.nasaclient.utils

import android.view.View
import androidx.annotation.StringRes
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.ghuljr.nasaclient.ui.common.ResourceError
import com.google.android.material.snackbar.Snackbar

fun <VIEW : BaseView<PRESENTER>, PRESENTER : BasePresenter<VIEW>> VIEW.makeSnackbar(
    view: View,
    @StringRes errorTextRes: Int,
    @StringRes buttonTextRes: Int,
    onButtonClicked: (() -> Unit)? = null
) {
    Snackbar.make(view, errorTextRes, Snackbar.LENGTH_INDEFINITE)
        .setAction(buttonTextRes) { onButtonClicked?.invoke() }
        .show()
}
