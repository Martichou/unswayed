package me.martichou.unswayed.utils

import android.content.SharedPreferences
import me.martichou.unswayed.models.retrofit.AccessToken

class TokenManager constructor(prefs: SharedPreferences) {

    private val editor: SharedPreferences.Editor = prefs.edit()

    var token: AccessToken? = null

    init {
        token = AccessToken(
            tokenType = prefs.getInt("TOKEN_TYPE", -1),
            accessToken = prefs.getString("ACCESS_TOKEN", null),
            refreshToken = prefs.getString("REFRESH_TOKEN", null),
            expireAt = prefs.getString("EXPIRE_AT", null)
        )
    }

    fun saveToken(token: AccessToken) {
        editor.putInt("TOKEN_TYPE", token.tokenType).commit()
        editor.putString("ACCESS_TOKEN", token.accessToken).commit()
        editor.putString("REFRESH_TOKEN", token.refreshToken).commit()
        editor.putString("EXPIRE_AT", token.expireAt).commit()
        this@TokenManager.token =
            AccessToken(
                tokenType = token.tokenType,
                accessToken = token.accessToken,
                refreshToken = token.refreshToken,
                expireAt = token.expireAt
            )
    }

    fun deleteToken() {
        editor.remove("TOKEN_TYPE").commit()
        editor.remove("ACCESS_TOKEN").commit()
        editor.remove("REFRESH_TOKEN").commit()
        editor.remove("EXPIRE_AT").commit()
        this@TokenManager.token = null
    }

    companion object : SingletonHolder<TokenManager, SharedPreferences>(::TokenManager)

}