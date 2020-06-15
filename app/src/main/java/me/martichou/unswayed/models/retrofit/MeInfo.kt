package me.martichou.unswayed.models.retrofit

import com.squareup.moshi.Json

data class MeInfo(
    @field:Json(name = "email") val email: String? = null
)