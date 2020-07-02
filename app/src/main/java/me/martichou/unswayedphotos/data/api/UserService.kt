package me.martichou.unswayedphotos.data.api

import me.martichou.unswayedphotos.data.model.api.ReturnImageInfo
import me.martichou.unswayedphotos.data.model.api.ReturnMe
import retrofit2.Response
import retrofit2.http.GET

interface UserService {

    @GET("/api/users/me")
    suspend fun me(): ReturnMe

    @GET("/api/users/mine")
    suspend fun uploaded(): Response<List<ReturnImageInfo>>

}