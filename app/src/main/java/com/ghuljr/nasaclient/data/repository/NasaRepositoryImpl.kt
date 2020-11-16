package com.ghuljr.nasaclient.data.repository

import android.util.Log
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.data.source.remote.NasaService
import com.ghuljr.nasaclient.data.source.storage.StorageManager
import com.ghuljr.nasaclient.ui.common.NetworkError
import com.ghuljr.nasaclient.utils.DAY_TIMESTAMP
import com.ghuljr.nasaclient.utils.isDateExpired
import io.reactivex.Observable
import io.reactivex.Single

class NasaRepositoryImpl(
    private val nasaService: NasaService,
    private val storageManager: StorageManager
) : NasaRepository {

    override fun fetchApod(): Single<Resource<ApodModel>> = nasaService.fetchApod()
        .map { Resource.Success(it) as Resource<ApodModel> }
        .onErrorReturn {
            Log.e(TAG, "Fetch APoD error.", it)
            Resource.Error(NetworkError.InternetConnectionError)
        }

    override fun updateApod(): Observable<Resource<ApodModel>> =
        storageManager.getApodsSortedByDate().take(1)
            .flatMap {
                if (it.isEmpty() || it.first().date.isDateExpired(DAY_TIMESTAMP))
                    fetchApod().toObservable()
                else
                    Observable.just(Resource.Error(NetworkError.UpToDateError))
            }.map {
                it.data?.let { storageManager.insertApod(it) }
                it
            }

    override fun getLatestApod(): Observable<ApodModel> {
        return storageManager.getLatestApod()
    }

    override fun getApodList(): Observable<List<ApodModel>> {
        return storageManager.getApodsSortedByDate()
    }

    override fun insertApod(apod: ApodModel): Long {
        return storageManager.insertApod(apod)
    }



    companion object {
        private const val TAG = "NasaRepositoryImpl"
    }
}