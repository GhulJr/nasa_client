package com.ghuljr.nasaclient.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
@JsonClass(generateAdapter = true)
data class NasaMediaModel(
    @Id var id: Long = 0,
    @Json(name = "nasa_id") val nasaId: String,
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "date_created") val date: String,
    val description: String,
    val title: String,
    val center: String
    //TODO: apply keywords
)