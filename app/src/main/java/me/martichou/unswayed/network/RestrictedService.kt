package me.martichou.unswayed.network

import me.martichou.unswayed.models.retrofit.MeInfo
import me.martichou.unswayed.models.retrofit.Mine
import retrofit2.Call
import retrofit2.http.GET

interface RestrictedService {

    @GET("/api/users/me")
    fun me(): Call<MeInfo>

    @GET("/api/users/mine")
    fun mine(): Call<List<Mine>>

}