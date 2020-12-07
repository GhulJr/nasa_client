package com.ghuljr.nasaclient.data.source.remote.service

import com.ghuljr.nasaclient.data.model.ApodModel
import io.reactivex.Single
import retrofit2.http.GET

interface ApodService {

    @GET("/planetary/apod")
    fun fetchApod(): Single<ApodModel>
}