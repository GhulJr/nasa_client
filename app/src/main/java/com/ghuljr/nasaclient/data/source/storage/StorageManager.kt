package com.ghuljr.nasaclient.data.source.storage

import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.data.model.ApodModel_
import com.ghuljr.nasaclient.utils.dateToTimestamp
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Single

class StorageManager(
    private val apodBox: Box<ApodModel>
) {

    fun getLatestApod(): Single<ApodModel> = RxQuery.single(
        apodBox.query().sort { o1, o2 -> (o1.date.dateToTimestamp() - o2.date.dateToTimestamp()).toInt() }.build()
    )
        .map { it.first() }


}