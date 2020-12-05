package com.ghuljr.nasaclient.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class NasaMediaModel(
    @Id var id: Long = 0,
    val nasaId: String,
    val mediaType: String,
    val date: String,
    val description: String,
    val title: String,
    val center: String,
    val thumbnailUrl: String
)