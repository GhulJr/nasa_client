package com.ghuljr.nasaclient.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApodModel(
    var id: Long = 0,
    val date: String,
    val explanation: String,
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
)