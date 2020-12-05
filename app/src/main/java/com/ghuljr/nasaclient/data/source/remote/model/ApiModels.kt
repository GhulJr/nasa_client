package com.ghuljr.nasaclient.data.source.remote.model

import com.ghuljr.nasaclient.data.model.NasaMediaModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Collection(
    val items: List<Item<NasaMediaModel>>,
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


