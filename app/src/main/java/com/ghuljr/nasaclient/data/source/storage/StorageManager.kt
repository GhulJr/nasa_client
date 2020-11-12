package com.ghuljr.nasaclient.data.source.storage

import com.ghuljr.nasaclient.data.model.ApodModel
import io.objectbox.Box

class StorageManager(
    private val apodBox: Box<ApodModel>
) {

}