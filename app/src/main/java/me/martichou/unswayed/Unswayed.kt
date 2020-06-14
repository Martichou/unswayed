package me.martichou.unswayed

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

class Unswayed : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Stetho.initializeWithDefaults(this)
    }

}