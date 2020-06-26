package me.martichou.unswayed.network

import me.martichou.unswayed.utils.TokenManager
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitBuilder {

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://172.104.249.116:8080/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        return@lazy getRetrofit().create(AuthService::class.java)
    }

    private fun getRetrofitAuth(tokenManager: TokenManager): Retrofit {
        val tokenValue = tokenManager.token?.accessToken
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request: Request = chain.request()
                if (!tokenValue.isNullOrEmpty()) {
                    val builder: Request.Builder = request.newBuilder()
                    builder.addHeader("Authorization", "Bearer $tokenValue")
                    request = builder.build()
                }
                chain.proceed(request)
            }.authenticator(Authenticator.getInstance(tokenManager)).build()

        return Retrofit.Builder()
            .baseUrl("http://172.104.249.116:8080/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun restrictedService(tokenManager: TokenManager): RestrictedService {
        return getRetrofitAuth(tokenManager).create(RestrictedService::class.java)
    }
}