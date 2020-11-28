package com.ghuljr.nasaclient.data.repository

import android.util.Log
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.data.source.remote.NasaService
import com.ghuljr.nasaclient.data.source.storage.StorageManager
import com.ghuljr.nasaclient.data.source.toVoid
import com.ghuljr.nasaclient.ui.common.InternetConnectionError
import com.ghuljr.nasaclient.ui.common.NetworkError
import com.ghuljr.nasaclient.ui.common.UpToDateError
import com.ghuljr.nasaclient.utils.DAY_TIMESTAMP
import com.ghuljr.nasaclient.utils.isDateExpired
import io.reactivex.Observable
import io.reactivex.Single

class NasaRepositoryImpl(
    private val nasaService: NasaService,
    private val storageManager: StorageManager
) : NasaRepository {

    private val isApodOutdatedObservable: Observable<Boolean> =
        storageManager.getApodsSortedByDate()
            .map { it.isEmpty() || it.first().date.isDateExpired(DAY_TIMESTAMP) }
            .distinctUntilChanged()

    override fun fetchApod(): Single<Resource<ApodModel>> = nasaService.fetchApod()
        .map { Resource.Success(it) as Resource<ApodModel> }
        .onErrorReturn {
            Log.e(TAG, "Fetch APoD error.", it)
            Resource.Error(InternetConnectionError)
        }

    override fun updateApod(): Observable<Resource<Void>> = Observable.just(Unit)
        .switchMap {
            isApodOutdatedObservable
                .flatMap {
                    if (it) fetchApod().toObservable()
                    else Observable.just(Resource.Error(UpToDateError))
                }
        }
        .flatMap {
            it.data?.let {
                storageManager.insertApod(it)
                    .map {
                        if (it > 0) Resource.Success()
                        else Resource.Error(UpToDateError)
                    }
            } ?: Observable.just(it.toVoid())
        }


    override fun getLatestApod(): Observable<ApodModel> {
        return storageManager.getLatestApod()
            .replay(1)
            .refCount()
    }

    override fun getApodList(): Observable<List<ApodModel>> {
        return storageManager.getApodsSortedByDate()
            .replay(1)
            .refCount()
    }

    override fun getApodById(id: Long): Observable<ApodModel> {
        return storageManager.getApodById(id)
            .replay(1)
            .refCount()
    }

    companion object {
        private const val TAG = "NasaRepositoryImpl"
    }
}