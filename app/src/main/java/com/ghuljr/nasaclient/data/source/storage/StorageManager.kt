package com.ghuljr.nasaclient.data.source.storage

import android.util.Log
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.utils.dateToTimestamp
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.Single

class StorageManager(private val apodBox: Box<ApodModel>) {

    fun getLatestApod(): Observable<ApodModel> = RxQuery.observable(
        apodBox.query().sort { o1, o2 -> (o1.date.dateToTimestamp() - o2.date.dateToTimestamp()).toInt() }.build()
    )
        .filter { it.isNotEmpty() }
        .map { it.first() }

    companion object {
        private const val TAG = "StorageManager"
    }
}