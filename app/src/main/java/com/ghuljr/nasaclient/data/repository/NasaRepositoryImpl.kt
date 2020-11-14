package com.ghuljr.nasaclient.data.repository

import android.util.Log
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.data.source.remote.NasaService
import com.ghuljr.nasaclient.data.source.storage.StorageManager
import com.ghuljr.nasaclient.utils.DAY_TIMESTAMP
import com.ghuljr.nasaclient.utils.isDateExpired
import com.ghuljr.nasaclient.utils.toVoid
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
            Log.e(TAG, "Fetch APoD error.", it)
            Resource.create(it)
        }

    override fun getLatestApod(): Observable<ApodModel> {
        return storageManager.getLatestApod()
    }

    override fun updateApodList(): Observable<Resource<Void>> = storageManager.getApods()
        .flatMap {
            if (it.isEmpty() || it.first().date.isDateExpired(DAY_TIMESTAMP))
                fetchApod().toObservable()
            else
                Observable.just(Resource.Success(null))
        }. map {
            it.data?.let { storageManager.insertApod(it)}
            it.toVoid()
        }

    companion object {
        private const val TAG = "NasaRepositoryImpl"
    }
}