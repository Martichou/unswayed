package me.martichou.unswayedphotos.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.martichou.unswayedphotos.data.api.AuthService
import me.martichou.unswayedphotos.data.api.UserService
import me.martichou.unswayedphotos.data.room.AppDatabase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.KeyStore
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, CoreDataModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideUserService(okHttpClient: OkHttpClient) =
        provideServiceAuth(okHttpClient, UserService::class.java)

    @Singleton
    @Provides
    fun provideAuthService() = provideService(AuthService::class.java)

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideImageDao(db: AppDatabase) = db.imageDao()

    @Singleton
    @Provides
    fun provideSharedPreference(app: Application) =
        app.getSharedPreferences("unswayed", Context.MODE_PRIVATE)

    @CoroutineScropeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

    @Singleton
    @Provides
    fun provideSyncDao(db: AppDatabase) = db.syncDao()

    @Singleton
    @Provides
    fun provideKeyStore() = KeyStore.getInstance("AndroidKeyStore").also { it.load(null) }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.unswayed.app:8080/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun createRetrofitAuth(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.unswayed.app:8080/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private fun <T> provideService(clazz: Class<T>): T {
        return createRetrofit().create(clazz)
    }

    private fun <T> provideServiceAuth(okHttpClient: OkHttpClient, clazz: Class<T>): T {
        return createRetrofitAuth(okHttpClient).create(clazz)
    }

}