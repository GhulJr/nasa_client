package com.ghuljr.nasaclient.data.repository

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.data.source.remote.NasaService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class NasaRepositoryImpl(
    private val nasaService: NasaService
): NasaRepository {
    override fun fetchApod(): Single<Resource<ApodModel>> = nasaService.fetchApod()
        .subscribeOn(Schedulers.io())
        .map { Resource.create(it) }
        .onErrorReturn { Resource.create(it) }
}