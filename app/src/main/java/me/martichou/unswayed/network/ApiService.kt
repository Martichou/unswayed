package me.martichou.unswayed.network

import me.martichou.unswayed.models.AccessToken
import me.martichou.unswayed.models.LoginData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("auth")
    fun auth(
        @Body body: LoginData
    ): Call<AccessToken>

}