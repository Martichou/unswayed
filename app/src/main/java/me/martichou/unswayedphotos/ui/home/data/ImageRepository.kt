package me.martichou.unswayedphotos.ui.home.data

import me.martichou.unswayedphotos.data.resultLiveData
import me.martichou.unswayedphotos.data.room.ImageDaoAsync
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
    private val dao: ImageDaoAsync,
    private val remoteSource: ImageRemoteDataSource
) {
    val images = resultLiveData(
        databaseQuery = { dao.getImages() },
        networkCall = { remoteSource.fetchData() },
        saveCallResult = {
            // TODO
        }
    )
}