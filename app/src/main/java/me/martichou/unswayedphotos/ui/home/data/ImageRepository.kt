package me.martichou.unswayedphotos.ui.home.data

import android.app.Application
import android.content.Intent
import me.martichou.unswayedphotos.data.resultLiveData
import me.martichou.unswayedphotos.service.MyService
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
    private val appContext: Application,
    private val dao: ImageDao,
    private val remoteSource: ImageRemoteDataSource
) {
    val images = resultLiveData(
        databaseQuery = { dao.getImages() },
        networkCall = { remoteSource.fetchData() },
        saveCallResult = {
            val intent = Intent(appContext, MyService::class.java).also { da ->
                da.putParcelableArrayListExtra("list", it as ArrayList)
            }
            appContext.startForegroundService(intent)
        }
    )
}