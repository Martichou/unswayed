package me.martichou.unswayed.network

import me.martichou.unswayed.models.retrofit.RefreshData
import me.martichou.unswayed.utils.SingletonHolder
import me.martichou.unswayed.utils.TokenManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class Authenticator constructor(private val tokenManager: TokenManager) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 3) {
            return null
        }

        val authService = RetrofitBuilder.authService
        val resp = authService.refresh(
            RefreshData(
                tokenManager.token?.refreshToken
            )
        ).execute()

        return if (resp.isSuccessful) {
            resp.body()?.let {
                tokenManager.saveToken(it)
            }
            response.request().newBuilder()
                .header("Authorization", "Bearer ${tokenManager.token?.accessToken}").build()
        } else {
            null
        }
    }

    private fun responseCount(response: Response): Int {
        if (response.priorResponse() == null)
            return 1
        var result = 1
        var responseMut = response
        while (responseMut.priorResponse().also { responseMut = it!! } != null) {
            result++
        }
        return result
    }

    companion object : SingletonHolder<Authenticator, TokenManager>(::Authenticator)

}