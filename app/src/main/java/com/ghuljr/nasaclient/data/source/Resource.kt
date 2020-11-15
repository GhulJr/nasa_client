package com.ghuljr.nasaclient.data.source

import com.ghuljr.nasaclient.ui.common.ResourceError
import retrofit2.Response

sealed class Resource<T>(
    val data: T? = null,
    val error: ResourceError? = null
) {

    class Success<T>(data: T) : Resource<T>(data = data)
    class Loading <T>: Resource<T>()
    class Error<T>(error: ResourceError): Resource<T>(error = error)
}

