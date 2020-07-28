package me.martichou.unswayedphotos.di

import dagger.Module
import dagger.Provides
import me.martichou.unswayedphotos.data.api.AuthService
import me.martichou.unswayedphotos.data.api.Authenticator
import me.martichou.unswayedphotos.util.TokenManager
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

@Module
class CoreDataModule {
    @Provides
    fun provideOkHttpClient(tokenManager: TokenManager, authService: AuthService): OkHttpClient {
        val tokenValue = tokenManager.token?.accessToken
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                var request: Request = chain.request()
                if (!tokenValue.isNullOrEmpty()) {
                    val builder: Request.Builder = request.newBuilder()
                    builder.addHeader("Authorization", "Bearer $tokenValue")
                    request = builder.build()
                }
                chain.proceed(request)
            }.authenticator(Authenticator(tokenManager, authService)).build()
    }
}