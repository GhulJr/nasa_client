package com.ghuljr.nasaclient.data.source.remote.service

import com.ghuljr.nasaclient.BuildConfig
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.source.remote.ApiResponse
import com.ghuljr.nasaclient.data.source.remote.NasaSearchResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApodService {

    @GET("/planetary/apod")
    fun fetchApod(): Single<ApodModel>
}