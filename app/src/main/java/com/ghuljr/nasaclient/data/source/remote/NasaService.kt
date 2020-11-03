package com.ghuljr.nasaclient.data.source.remote

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.source.Resource
import io.reactivex.Single
import retrofit2.http.GET

interface NasaService {

    @GET("/planetary/apod")
    fun fetchApod(): Single<Resource<ApodModel>>
}