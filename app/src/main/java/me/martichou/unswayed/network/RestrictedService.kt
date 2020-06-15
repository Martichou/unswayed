package me.martichou.unswayed.network

import me.martichou.unswayed.models.retrofit.MeInfo
import retrofit2.Call
import retrofit2.http.GET

interface RestrictedService {

    @GET("/api/users/me")
    fun me(): Call<MeInfo>

}