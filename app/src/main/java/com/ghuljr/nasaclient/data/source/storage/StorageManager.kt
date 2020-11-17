package com.ghuljr.nasaclient.data.source.storage

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.model.ApodModel_
import com.ghuljr.nasaclient.utils.dateToTimestamp
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class StorageManager(private val apodBox: Box<ApodModel>) {

    fun getApodsSortedByDate(): Observable<List<ApodModel>> = RxQuery.observable(
        apodBox.query().sort { o1, o2 -> (o2.date.dateToTimestamp() - o1.date.dateToTimestamp()).toInt() }.build()
    ).subscribeOn(Schedulers.io())

    fun getApodsSortedByDateSingle(): Single<List<ApodModel>> = RxQuery.single(
        apodBox.query().sort { o1, o2 -> (o2.date.dateToTimestamp() - o1.date.dateToTimestamp()).toInt() }.build()
    ).subscribeOn(Schedulers.io())

    fun getLatestApod(): Observable<ApodModel> = getApodsSortedByDate()
        .filter { it.isNotEmpty() }
        .map { it.first() }

    fun getApodById(id: Long): Observable<ApodModel> = Observable.just(apodBox[id])
        .subscribeOn(Schedulers.io())

    fun insertApod(apod: ApodModel): Long = apodBox.put(apod) //TODO: handle adding the same item twice

    companion object {
        private const val TAG = "StorageManager"
    }
}