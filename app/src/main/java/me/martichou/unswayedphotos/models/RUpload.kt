package me.martichou.unswayedphotos.models

import com.squareup.moshi.Json

data class RUpload(
    @field:Json(name = "realname") val realname: String,
    @field:Json(name = "filename") val filename: String
)