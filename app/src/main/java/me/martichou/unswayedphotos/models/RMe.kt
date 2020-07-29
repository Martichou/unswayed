package me.martichou.unswayedphotos.models

import com.squareup.moshi.Json

data class RMe(
    @field:Json(name = "email") val email: String
)