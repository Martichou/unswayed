package me.martichou.unswayedphotos.data.api

import me.martichou.unswayedphotos.models.RefreshPayload
import me.martichou.unswayedphotos.util.TokenManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class Authenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val authService: AuthService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 3) {
            return null
        }

        val resp = tokenManager.token?.let {
            authService.refresh(RefreshPayload(it.refreshToken)).execute()
        } ?: return null

        return if (resp.isSuccessful) {
            resp.body()?.let { tokenManager.saveToken(it) }
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

}