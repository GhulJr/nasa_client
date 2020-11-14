package com.ghuljr.nasaclient.data.repository

import android.util.Log
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.data.source.remote.NasaService
import com.ghuljr.nasaclient.data.source.storage.StorageManager
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class NasaRepositoryImpl(
    private val nasaService: NasaService,
    private val storageManager: StorageManager
) : NasaRepository {
    override fun fetchApod(): Single<Resource<ApodModel>> = nasaService.fetchApod()
        .subscribeOn(Schedulers.io())
        .map { Resource.create(it) }
        .onErrorReturn {
            Log.e(TAG, "Fetch APoD error", it)
            Resource.create(it)
        }

    override fun getApod(): Observable<ApodModel> {
        return storageManager.getLatestApod()
    }


    companion object {
        private const val TAG = "NasaRepositoryImpl"
    }
}