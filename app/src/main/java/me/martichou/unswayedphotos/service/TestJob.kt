package me.martichou.unswayedphotos.service

import android.app.job.JobParameters
import android.app.job.JobService
import dagger.android.AndroidInjection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.martichou.unswayedphotos.data.Result
import me.martichou.unswayedphotos.data.api.UserService
import me.martichou.unswayedphotos.data.model.api.ReturnImageInfo
import me.martichou.unswayedphotos.data.room.SyncDao
import me.martichou.unswayedphotos.util.encrypt
import me.martichou.unswayedphotos.util.getImages
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import java.security.KeyStore
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TestJob : JobService() {

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var dao: SyncDao

    @Inject
    lateinit var keyStore: KeyStore

    private lateinit var job: Job

    override fun onStopJob(params: JobParameters?): Boolean {
        Timber.d("DBG: Job stopped before completion")
        job.cancel()
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.d("DBG: Job started at ${Date()}")

        job = CoroutineScope(Dispatchers.IO).launch {
            uploadPhotos()
            Timber.d("DBG: Job finished")
            jobFinished(params, false)
        }

        return true
    }

    private suspend fun uploadPhotos() {
        val response = getRemoteImages<List<ReturnImageInfo>>()
        val remImg: List<ReturnImageInfo>?
        if (response.status == Result.Status.SUCCESS) {
            remImg = response.data
        } else {
            return
        }
        val remImgSimpl = remImg?.map { it.realname } as ArrayList<String>
        Timber.d("DBG: Remote $remImgSimpl")
        val allImg = getImages(this, listOf("Camera"))
        val locImg = dao.getImagesSync()
        val locImgSimpl = locImg.map { it.getUploadName() }

        allImg.forEach let@{
            Timber.d("DBG: WTF ${!remImgSimpl.contains(it.getUploadName())}")
            if (!remImgSimpl.contains(it.getUploadName())) {
                Timber.d("DBG: Upload process starting for ${it.getUploadName()}")
                // Start the encryption process
                val files = it.encrypt(this, keyStore.getKey("aesKey", null)) ?: return@let
                val fullsize = files.d1 as File
                val thumbnail = files.d2 as File
                Timber.d("DBG: > Encryption finished")
                // Upload the files
                val requestFileFull = RequestBody.create(MediaType.parse("image/*"), fullsize)
                val bodyFull =
                    MultipartBody.Part.createFormData("images", fullsize.name, requestFileFull)

                val requestFileThumbnail = RequestBody.create(MediaType.parse("image/*"), thumbnail)
                val bodyThumbnail = MultipartBody.Part.createFormData(
                    "images",
                    thumbnail.name,
                    requestFileThumbnail
                )

                Timber.d("DBG: > Starting the real upload")
                val resp = userService.uploadImage(bodyFull, bodyThumbnail)
                if (resp.isSuccessful) {
                    Timber.d("DBG: Files upload correctly ${resp.body()}")
                } else {
                    Timber.d("DBG: Files upload failed ${resp.code()} ${resp.message()}")
                }
                Timber.d("DBG : > Finished, new file?")

                if (it in locImg) {
                    dao.insertImage(it.apply { backed = true })
                } else {
                    if (it.getUploadName() in locImgSimpl) {
                        dao.insertImage(it.apply { backed = false })
                        // Else this mean the image if just not on the localDatabase so we add it
                    } else {
                        dao.insertImage(it)
                    }
                }
            }
        }
    }

    private suspend fun <T> getRemoteImages(): Result<T> {
        try {
            val response = userService.uploaded()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.success(body) as Result<T>
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Result<T> {
        Timber.e(message)
        return Result.error("Network call has failed for a following reason: $message")
    }

}