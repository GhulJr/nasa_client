package com.ghuljr.nasaclient.data.source.remote

import com.ghuljr.nasaclient.data.model.ApodModel
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface NasaService {

    @GET("/planetary/apod")
    fun fetchApod(): Single<Response<ApodModel>>
}