package com.ghuljr.nasaclient.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
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

fun Context.expandKeyboard() {
   val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}