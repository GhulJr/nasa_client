package com.ghuljr.nasaclient.data.source.remote.service

import com.ghuljr.nasaclient.BuildConfig
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.source.remote.model.ApiResponse
import com.ghuljr.nasaclient.data.source.remote.model.NasaSearchResult
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaService {

    @GET("${BuildConfig.BASE_APOD_URL}/planetary/apod")
    fun fetchApod(): Single<ApodModel>

    @GET("${BuildConfig.BASE_NASA_IMAGE_URL}/search")
    fun searchNasaMedia(@Query("q") query: String): Single<ApiResponse<NasaSearchResult>>
}