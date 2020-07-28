package me.martichou.unswayedphotos

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.martichou.unswayedphotos.util.CrashReportingTree
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        else Timber.plant(CrashReportingTree())
    }

}