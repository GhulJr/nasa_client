package com.ghuljr.nasaclient.data.repository

import android.util.Log
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.model.NasaMediaModel
import com.ghuljr.nasaclient.data.source.Resource
import com.ghuljr.nasaclient.data.source.remote.toNasaMediaModel
import com.ghuljr.nasaclient.data.source.remote.service.ApodService
import com.ghuljr.nasaclient.data.source.remote.service.NasaMediaService
import com.ghuljr.nasaclient.data.source.storage.StorageManager
import com.ghuljr.nasaclient.data.source.toVoid
import com.ghuljr.nasaclient.ui.common.InternetConnectionError
import com.ghuljr.nasaclient.ui.common.UpToDateError
import com.ghuljr.nasaclient.utils.DAY_TIMESTAMP
import com.ghuljr.nasaclient.utils.isDateExpired
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class NasaRepositoryImpl(
    private val apodService: ApodService,
    private val nasaMediaService: NasaMediaService,
    private val storageManager: StorageManager
) : NasaRepository {

    /* Apod */

    private val isApodOutdatedSingle: Single<Boolean> = storageManager.getApodsSortedByDateSingle()
        .map { it.isEmpty() || it.first().date.isDateExpired(DAY_TIMESTAMP) }

    override fun fetchApod(): Single<Resource<ApodModel>> = apodService.fetchApod()
        .map { Resource.Success(it) as Resource<ApodModel> }
        .onErrorReturn {
            Log.e(TAG, "Fetch APoD error.", it)
            Resource.Error(InternetConnectionError)
        }

    override fun updateApod(): Observable<Resource<Void>> = Observable.just(Unit)
        .subscribeOn(Schedulers.io())
        .flatMap {
            isApodOutdatedSingle.toObservable()
                .flatMap {
                    if (it) fetchApod().toObservable()
                    else Observable.just(Resource.Error(UpToDateError))
                }
        }
        .flatMap {
            it.data?.let {
                storageManager.insertApod(it).map {
                    if (it > 0) Resource.Success()
                    else Resource.Error(UpToDateError)
                }
            } ?: Observable.just(it.toVoid())
        }
        .replay(1)
        .refCount()

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

    /* NasaMedia */

    override fun searchNasaMedia(query: String): Single<Resource<List<NasaMediaModel>>> =
        nasaMediaService.searchNasaMedia(query)
            .subscribeOn(Schedulers.io())
            .map { it.collection }
            .map { response -> response.items.map { it.toNasaMediaModel() } }
            .map { Resource.Success(it) as Resource<List<NasaMediaModel>> }
            .onErrorReturn {
                Log.e("TAG", "Fetch searching nasa media result error.", it)
                Resource.Error(InternetConnectionError)
            }


    companion object {
        private const val TAG = "NasaRepositoryImpl"
    }
}