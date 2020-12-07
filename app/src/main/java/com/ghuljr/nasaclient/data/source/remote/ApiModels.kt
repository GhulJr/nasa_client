package com.ghuljr.nasaclient.data.source.remote

import com.ghuljr.nasaclient.data.model.NasaMediaModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ApiResponse(val collection: Collection)

@JsonClass(generateAdapter = true)
data class Collection(
    val items: List<Item>,
    val version: String,
    val links: List<Link>?,
    val href: String
)

@JsonClass(generateAdapter = true)
data class Item(
    val data: List<NasaSearchResult>,
    val href: String,
    val links: List<Link>?
)

@JsonClass(generateAdapter = true)
data class Metadata(
    @Json(name = "total_hits") val totalHits: Long?
)

@JsonClass(generateAdapter = true)
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
    val description: String = "",
    val title: String = "",
    val center: String = ""
    //TODO: apply keywords
)

fun Item.toNasaMediaModel(): NasaMediaModel = NasaMediaModel(
    nasaId = data.first().nasaId,
    mediaType = data.first().mediaType,
    description = data.first().description,
    title = data.first().title,
    center = data.first().center,
    date = data.first().date,
    thumbnailUrl = links?.first()?.href ?: ""
)


