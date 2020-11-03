package com.ghuljr.nasaclient.data.source

import retrofit2.Response

sealed class Resource<T>(
    val data: T? = null,
    val code: Int? = null,
    val message: String? = null
) {
    companion object {
        private val unknownError = "unknown-error"

        fun <T> create(t: Throwable): Resource<T> {
            return Error(
                t.message ?:
                unknownError
            )
        }
        fun <T> create(response: Response<T>): Resource<T> {
            return if (response.isSuccessful) {
                Success(
                    response.body(),
                    response.code()
                )
            } else {
                val msg = response.errorBody()?.string() ?: unknownError
                Error(
                    msg,
                    response.code()
                )
            }
        }
    }

    class Success<T>(data: T?, code: Int? = null) : Resource<T>(data, code)
    class Loading<T>(data: T? = null, code: Int? = null) : Resource<T>(data, code)
    class Error<T>(message: String, code: Int? = null, data: T? = null) : Resource<T>(data, code, message)
}
