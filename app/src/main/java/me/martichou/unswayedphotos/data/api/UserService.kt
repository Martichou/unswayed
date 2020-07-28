package me.martichou.unswayedphotos.data.api

import me.martichou.unswayedphotos.data.model.api.ReturnImageInfo
import me.martichou.unswayedphotos.data.model.api.ReturnMe
import me.martichou.unswayedphotos.data.model.api.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserService {

    @GET("/api/users/me")
    suspend fun me(): ReturnMe

    @GET("/api/users/mine")
    suspend fun uploaded(): Response<List<ReturnImageInfo>>

    @Multipart
    @POST("/api/files/upload")
    suspend fun uploadImage(
        @Part fileFull: MultipartBody.Part,
        @Part fileThumbnail: MultipartBody.Part
    ): Response<List<UploadResponse>>

}