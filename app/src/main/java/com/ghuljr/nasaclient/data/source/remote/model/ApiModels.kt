package com.ghuljr.nasaclient.data.source.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ApiResponse(
    val items: List<Item<NasaSearchResult>>,
    val version: String,
    val links: List<Links>,
    val href: String
)

data class Item<T>(
    val data: List<T>,
    val href: String,
    val links: List<Links>
)

@JsonClass(generateAdapter = true)
data class Metadata(
    @Json(name = "total_hits") val totalHits: Long?
)

data class Links(
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


