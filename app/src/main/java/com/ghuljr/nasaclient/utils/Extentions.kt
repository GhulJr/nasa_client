package com.ghuljr.nasaclient.utils

import com.ghuljr.nasaclient.data.source.Resource

fun<T> Resource<T>.toVoid() : Resource<Void> = when(this) {
    is Resource.Success -> Resource.Success(null)
    is Resource.Loading -> Resource.Loading()
    is Resource.Error -> Resource.Error("")
}