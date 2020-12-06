package com.ghuljr.nasaclient.data.source.remote

import com.ghuljr.nasaclient.data.model.NasaMediaModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    val items: List<Item<T>>,
    val version: String,
    val links: List<Link>,
    val href: String
)

data class Item<T>(
    val data: T,
    val href: String,
    val link: Link
)

@JsonClass(generateAdapter = true)
data class Metadata(
    @Json(name = "total_hits") val totalHits: Long?
)

data class Link(
    val prompt: String?,
    val rel: String?,
    val href: String?,
    val render: String?
)

@JsonClass(generateAdapter = true)
data class NasaSearchResult(
    @Json(name = "nasa_id") val nasaId: String,
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "date_created") val date: String,
    val description: String,
    val title: String,
    val center: String
    //TODO: apply keywords
)

fun Item<out NasaSearchResult>.toNasaMediaModel(): NasaMediaModel = NasaMediaModel(
    nasaId = data.nasaId,
    mediaType = data.mediaType,
    description = data.description,
    title = data.title,
    center = data.center,
    date = data.date,
    thumbnailUrl = link.href ?: ""
)


