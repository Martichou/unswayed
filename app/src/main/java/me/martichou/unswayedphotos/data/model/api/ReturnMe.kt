package me.martichou.unswayedphotos.data.model.api

import com.squareup.moshi.Json

data class ReturnMe(
    @field:Json(name = "email") val email: String
)