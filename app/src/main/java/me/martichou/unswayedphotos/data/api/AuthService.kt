package me.martichou.unswayedphotos.data.api

import me.martichou.unswayedphotos.models.AccessToken
import me.martichou.unswayedphotos.models.LoginPayload
import me.martichou.unswayedphotos.models.RefreshPayload
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth")
    suspend fun auth(@Body body: LoginPayload): AccessToken

    @POST("refresh")
    fun refresh(@Body body: RefreshPayload): Call<AccessToken>

}