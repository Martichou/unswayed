package me.martichou.unswayed

import android.app.Application
import timber.log.Timber

class Unswayed : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}