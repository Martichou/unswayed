package me.martichou.unswayedphotos.ui.home.data

import me.martichou.unswayedphotos.data.api.BaseDataSource
import me.martichou.unswayedphotos.data.api.UserService
import javax.inject.Inject

class ImageRemoteDataSource @Inject constructor(private val service: UserService) :
    BaseDataSource() {

    suspend fun fetchData() = getResult { service.uploaded() }

}