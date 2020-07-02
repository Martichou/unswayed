package me.martichou.unswayedphotos.ui.home.data

import me.martichou.unswayedphotos.data.resultLiveData
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
    private val dao: ImageDao,
    private val remoteSource: ImageRemoteDataSource
) {
    val images = resultLiveData(
        databaseQuery = { dao.getImages() },
        networkCall = { remoteSource.fetchData() },
        saveCallResult = {
            Timber.d(it.toString())
            // Save the thumbnail locally (unencrypted) if there is any filename_thumbnail remotely
            // else save the full size image locally (unencrypted)
            // Then get the URI to that newly created file and add it to Room
            // this process might take a long time.

            // Solution 1: Create a service which will live as long as the list is not processed
            // that means image not already existing locally will be download and the others, existing locally
            // will be skipped for the unencrypted process and the download.
        }
    )
}