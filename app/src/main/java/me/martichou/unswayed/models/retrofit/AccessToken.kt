package me.martichou.unswayed.models.retrofit

import com.squareup.moshi.Json

data class AccessToken(
    @field:Json(name = "token_type") val tokenType: Int = -1,
    @field:Json(name = "access_token") val accessToken: String? = null,
    @field:Json(name = "refresh_token") val refreshToken: String? = null,
    @field:Json(name = "created_at") val createdAt: String? = null,
    @field:Json(name = "expire_at") val expireAt: String? = null
)