package me.martichou.unswayed.network

import me.martichou.unswayed.models.MeInfo
import retrofit2.http.GET

interface RestrictedService {

    @GET("/users/me")
    fun me(): MeInfo

}