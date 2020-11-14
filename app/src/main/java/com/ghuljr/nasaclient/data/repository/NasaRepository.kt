package com.ghuljr.nasaclient.data.repository

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.source.Resource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface NasaRepository {
    fun fetchApod(): Single<Resource<ApodModel>>
    fun getApod(): Observable<ApodModel>
}