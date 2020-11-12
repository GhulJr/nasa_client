package com.ghuljr.nasaclient.ui.common.error

import android.view.View
import androidx.annotation.StringRes
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.ui.base.mvp.BasePresenter
import com.ghuljr.nasaclient.ui.base.mvp.BaseView
import com.google.android.material.snackbar.Snackbar


sealed class Errors

sealed class ApodError(@StringRes val messageRes: Int, @StringRes val buttonRes: Int): Errors() {
    object Error : ApodError(R.string.apod_fetch_error, R.string.retry)
    object UpToDate: ApodError(R.string.apod_up_to_date, R.string.ok)
}

fun <VIEW : BaseView<PRESENTER>, PRESENTER : BasePresenter<VIEW>> VIEW.makeSnackbar(view: View, error: ApodError, onButtonClicked: () -> Unit) {
    Snackbar.make(view, error.messageRes, Snackbar.LENGTH_INDEFINITE)
        .setAction(error.buttonRes) { onButtonClicked() }
        .show()
}

