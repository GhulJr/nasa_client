package com.ghuljr.nasaclient.data.repository

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.model.NasaMediaModel
import com.ghuljr.nasaclient.data.source.Resource
import io.reactivex.Observable
import io.reactivex.Single

interface NasaRepository {
    fun fetchApod(): Single<Resource<ApodModel>>
    fun getLatestApod(): Observable<ApodModel>
    fun getApodList(): Observable<List<ApodModel>>
    fun getApodById(id: Long): Observable<ApodModel>
    fun updateApod(): Observable<Resource<Void>>
    fun searchNasaMedia(query: String): Single<List<NasaMediaModel>>
}