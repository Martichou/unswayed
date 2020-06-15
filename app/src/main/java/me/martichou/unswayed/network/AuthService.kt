package me.martichou.unswayed.network

import me.martichou.unswayed.models.retrofit.AccessToken
import me.martichou.unswayed.models.retrofit.LoginData
import me.martichou.unswayed.models.retrofit.RefreshData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth")
    suspend fun auth(@Body body: LoginData): AccessToken

    @POST("refresh")
    fun refresh(@Body body: RefreshData): Call<AccessToken>

}