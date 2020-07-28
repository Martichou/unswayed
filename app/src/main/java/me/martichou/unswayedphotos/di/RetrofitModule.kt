package me.martichou.unswayedphotos.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import me.martichou.unswayedphotos.data.api.AuthService
import me.martichou.unswayedphotos.data.api.Authenticator
import me.martichou.unswayedphotos.data.api.UserService
import me.martichou.unswayedphotos.util.TokenManager
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@InstallIn(ApplicationComponent::class)
@Module
object RetrofitModule {

    @Reusable
    @Provides
    fun provideUserService(okHttpClient: OkHttpClient) =
        provideServiceAuth(okHttpClient, UserService::class.java)

    @Reusable
    @Provides
    fun provideAuthService() = provideService(AuthService::class.java)

    @Reusable
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