package com.ghuljr.nasaclient.ui.common

import androidx.annotation.StringRes
import com.ghuljr.nasaclient.R

sealed class ResourceError()
object UpToDateError : ResourceError()

sealed class NetworkError : ResourceError()
    object ResponseError : NetworkError()
    object InternetConnectionError : NetworkError()



