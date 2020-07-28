package me.martichou.unswayedphotos.data.model.api

import com.squareup.moshi.Json

data class UploadResponse(
    @field:Json(name = "realname") val realname: String,
    @field:Json(name = "filename") val filename: String
)