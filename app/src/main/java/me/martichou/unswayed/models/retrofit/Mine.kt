package me.martichou.unswayed.models.retrofit

import com.squareup.moshi.Json

data class Mine(
    @field:Json(name = "realname") val realname: String? = null,
    @field:Json(name = "fakedname") val fakedname: String? = null,
    @field:Json(name = "created_at") val createdAt: String? = null
)