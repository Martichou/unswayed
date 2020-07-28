package me.martichou.unswayedphotos.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.android.AndroidInjection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.martichou.unswayedphotos.R
import me.martichou.unswayedphotos.data.model.api.ReturnImageInfo
import me.martichou.unswayedphotos.data.room.SyncDao
import me.martichou.unswayedphotos.util.getImages
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MyService : Service() {

    @Inject
    lateinit var dao: SyncDao

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1919, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val remImg = intent?.getParcelableArrayListExtra<ReturnImageInfo>("list")

        CoroutineScope(Dispatchers.IO).launch {

            val remSimpl = remImg!!.map { it.realname } as ArrayList<String> // Used for comparaison
            val allImg = getImages(
                this@MyService,
                listOf("Camera")
            ) // All Images on the phone (Camera Folder)
            val locImg = dao.getImagesSync() // All Images in the database
            val locImgSimpl = locImg.map { it.getUploadName() } // Same as above but in String

            allImg.forEach { rawLocal ->
                // Raw local have backed = false by default as it was just constructed
                // That mean if rawLocal is in locImg, the object of locImg is false too
                // We can check if the image is backed or not, if yes we update the obj.
                if (rawLocal in locImg) {
                    // We update the model and insert it (which replace it) if it pass the condition
                    if (rawLocal.getUploadName() in remSimpl) {
                        dao.insertImage(rawLocal.apply { backed = true })
                    }
                } else {
                    // If this pass the next condition it mean the backed is opposed (true in local)
                    if (rawLocal.getUploadName() in locImgSimpl) {
                        // So we check if it's still true and if not update the value
                        if (rawLocal.getUploadName() !in remSimpl) {
                            dao.insertImage(rawLocal.apply { backed = false })
                        }
                        // Else this mean the image if just not on the localDatabase so we add it
                    } else {
                        dao.insertImage(rawLocal)
                    }
                }

                // If the localImage is in the remoteList
                // We can remove it from the remoteList as it won't be useful to download it.
                if (rawLocal.getUploadName() in remSimpl) {
                    remImg.removeIf { it.realname == rawLocal.getUploadName() }
                }
            }

            remImg.forEach {
                Timber.d("DBG: This image is not on the local db and is backed $it")
                // Start downloading and decrypting it
            }

            this@MyService.stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Timber.d("DBG: Service is now destroyed")
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("sync_channel", "Sync", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private val notification by lazy {
        NotificationCompat.Builder(this, "sync_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Synchronization...")
            .setProgress(100, 0, true)
            .build()
    }

}