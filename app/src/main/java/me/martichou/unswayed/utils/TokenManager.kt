package me.martichou.unswayed.utils

import android.content.SharedPreferences
import me.martichou.unswayed.models.AccessToken


class TokenManager private constructor(private val prefs: SharedPreferences) {

    private val editor: SharedPreferences.Editor = prefs.edit()

    fun saveToken(token: AccessToken) {
        editor.putInt("TOKEN_TYPE", token.tokenType).commit()
        editor.putString("ACCESS_TOKEN", token.accessToken).commit()
        editor.putString("REFRESH_TOKEN", token.refreshToken).commit()
        editor.putString("EXPIRE_AT", token.expireAt).commit()
    }

    fun deleteToken() {
        editor.remove("TOKEN_TYPE").commit()
        editor.remove("ACCESS_TOKEN").commit()
        editor.remove("REFRESH_TOKEN").commit()
        editor.remove("EXPIRE_AT").commit()
    }

    val token: AccessToken
        get() {
            return AccessToken(
                tokenType = prefs.getInt("TOKEN_TYPE", -1),
                accessToken = prefs.getString("ACCESS_TOKEN", null),
                refreshToken = prefs.getString("REFRESH_TOKEN", null),
                expireAt = prefs.getString("EXPIRE_AT", null))
        }

    companion object {
        private lateinit var INSTANCE: TokenManager

        @Synchronized
        fun getInstance(prefs: SharedPreferences): TokenManager {
            if (!this::INSTANCE.isInitialized) {
                INSTANCE = TokenManager(prefs)
            }
            return INSTANCE
        }
    }

}