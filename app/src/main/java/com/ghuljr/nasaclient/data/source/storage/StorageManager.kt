package com.ghuljr.nasaclient.data.source.storage

import android.util.Log
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.model.ApodModel_
import com.ghuljr.nasaclient.utils.dateToTimestamp
import io.objectbox.Box
import io.objectbox.query.QueryBuilder
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class StorageManager(private val apodBox: Box<ApodModel>) {

    fun getApodsSortedByDate(): Observable<List<ApodModel>> = RxQuery.observable(
        apodBox.sorted().build()
    ).subscribeOn(Schedulers.io())

    fun getApodsSortedByDateSingle(): Single<List<ApodModel>> = RxQuery.single(
        apodBox.sorted().build()
    ).subscribeOn(Schedulers.io())

    fun getApodByDate(date: String): Single<List<ApodModel>> = RxQuery.single(
        apodBox.query().equal(ApodModel_.date, date).sorted().build()
    ).subscribeOn(Schedulers.io())

    fun getLatestApod(): Observable<ApodModel> = getApodsSortedByDate()
        .filter { it.isNotEmpty() }
        .map { it.first() }

    fun getApodById(id: Long): Observable<ApodModel> = Observable.just(apodBox[id])
        .subscribeOn(Schedulers.io())

    fun insertApod(apodModel: ApodModel): Observable<Long> = Observable.just(apodModel)
        .flatMap { apod ->
            getApodByDate(apod.date).toObservable()
                .map { it.isEmpty() }
                .map { if(it) apodBox.put(apod) else 0L }
        }

    companion object {
        private const val TAG = "StorageManager"
    }
}

private fun Box<ApodModel>.sorted(): QueryBuilder<ApodModel> = this.query().sort { o1, o2 -> (o2.date.dateToTimestamp() - o1.date.dateToTimestamp()).toInt() }
private fun QueryBuilder<ApodModel>.sorted(): QueryBuilder<ApodModel> = this.sort { o1, o2 -> (o2.date.dateToTimestamp() - o1.date.dateToTimestamp()).toInt() }