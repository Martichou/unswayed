package me.martichou.unswayedphotos.ui.home.data

import me.martichou.unswayedphotos.data.resultLiveData
import me.martichou.unswayedphotos.data.room.ImageDaoAsync
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(
    private val dao: ImageDaoAsync,
    private val remoteSource: RemoteDataSource
) {
    val images = resultLiveData(
        databaseQuery = { dao.getImages() },
        networkCall = { remoteSource.fetchData() },
        saveCallResult = {
            // TODO
        }
    )
}