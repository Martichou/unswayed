package me.martichou.unswayedphotos.data.api

import me.martichou.unswayedphotos.data.model.api.AccessToken
import me.martichou.unswayedphotos.data.model.api.CredentialsData
import me.martichou.unswayedphotos.data.model.api.RefreshCredentials
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth")
    suspend fun auth(@Body body: CredentialsData): AccessToken

    @POST("refresh")
    fun refresh(@Body body: RefreshCredentials): Call<AccessToken>

}