package me.martichou.unswayedphotos.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import me.martichou.unswayedphotos.data.room.AppDatabase
import java.security.KeyStore
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreference(app: Application): SharedPreferences =
        app.getSharedPreferences("unswayed", Context.MODE_PRIVATE)

    @Reusable
    @Provides
    fun provideKeyStore(): KeyStore = KeyStore.getInstance("AndroidKeyStore").also { it.load(null) }

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Reusable
    @Provides
    fun provideImageDao(db: AppDatabase) = db.imageDaoAsync()

    @Reusable
    @Provides
    fun provideSyncDao(db: AppDatabase) = db.imageDaoSync()

}