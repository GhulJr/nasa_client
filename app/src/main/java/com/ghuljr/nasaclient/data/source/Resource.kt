package com.ghuljr.nasaclient.data.source

sealed class Resource<T>(
    val body: T?,
    val code: Int?,
    val message: String?

) {

    class Success<T>(body: T, code: Int, message: String? = null): Resource<T>(body, code, message)
    class Loading<T>(body: T? = null, code: Int? = null, message: String? = null):  Resource<T>(body, code, message)
    class Error<T>(body: T? = null, code: Int? = null, message: String? = null):  Resource<T>(body, code, message)
}