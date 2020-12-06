package com.ghuljr.nasaclient.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
@JsonClass(generateAdapter = true)
data class ApodModel(
    @Id var id: Long = 0,
    val copyright: String = "",
    val date: String,
    val explanation: String,
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String,
    val hdurl: String
)