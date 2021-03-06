package me.martichou.unswayedphotos.data.api

import me.martichou.unswayedphotos.models.RMe
import me.martichou.unswayedphotos.models.RMine
import me.martichou.unswayedphotos.models.RUpload
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserService {

    @GET("/api/users/me")
    suspend fun me(): RMe

    @GET("/api/users/mine")
    suspend fun mine(): Response<List<RMine>>

    @Multipart
    @POST("/api/files/upload")
    suspend fun upload(
        @Part fileFull: MultipartBody.Part,
        @Part fileThumbnail: MultipartBody.Part
    ): Response<List<RUpload>>

}