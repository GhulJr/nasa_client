package com.ghuljr.nasaclient.ui.common

import androidx.annotation.StringRes
import com.ghuljr.nasaclient.R

sealed class ResourceError(@StringRes val messageRes: Int)

sealed class NetworkError(messageRes: Int) : ResourceError(messageRes = messageRes) {
    object ResponseError : NetworkError()
    object InternetConnectionError : NetworkError(R.string.error_internet_connection)
    object UpToDateError : NetworkError(R.string.error_up_to_date)
}

