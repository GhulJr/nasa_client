package com.ghuljr.nasaclient.data.repository

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.data.source.ResourceError
import io.reactivex.Observable
import io.reactivex.Single

interface NasaRepository {
    fun fetchApod(): Single<Resource<ApodModel, ResourceError>>
    fun getLatestApod(): Observable<ApodModel>
    fun getApodList(): Observable<List<ApodModel>>
    fun insertApod(apod: ApodModel): Long
}