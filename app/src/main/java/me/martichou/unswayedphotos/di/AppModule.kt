package me.martichou.unswayedphotos.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import me.martichou.unswayedphotos.data.api.AuthService
import me.martichou.unswayedphotos.data.api.UserService
import me.martichou.unswayedphotos.data.room.AppDatabase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.KeyStore
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreference(app: Application): SharedPreferences =
        app.getSharedPreferences("unswayed", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideKeyStore(): KeyStore = KeyStore.getInstance("AndroidKeyStore").also { it.load(null) }

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideImageDao(db: AppDatabase) = db.imageDaoAsync()

    @Singleton
    @Provides
    fun provideSyncDao(db: AppDatabase) = db.imageDaoSync()

}