package me.martichou.unswayedphotos.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import me.martichou.unswayedphotos.data.Result
import me.martichou.unswayedphotos.data.api.AuthService
import me.martichou.unswayedphotos.models.LoginPayload
import me.martichou.unswayedphotos.util.aesKeyProtection
import me.martichou.unswayedphotos.util.generateAesKeyFromPassword
import timber.log.Timber
import java.security.KeyStore

class PasswordViewModel @ViewModelInject constructor(
    private val authService: AuthService,
    private val keyStore: KeyStore
) : ViewModel() {

    fun processConnection(data: LoginPayload) = liveData(Dispatchers.IO) {
        emit(Result.loading())
        try {
            emit(Result.success(authService.auth(data)))
        } catch (exception: Exception) {
            Timber.e(exception)
            emit(Result.error(message = exception.message ?: "An error occured"))
        }
    }

    fun generateAndSaveAes(emailEE: ByteArray, pswdEE: String) {
        val aesKey = generateAesKeyFromPassword(pswdEE.toCharArray(), emailEE, 75666)
        keyStore.setEntry("aesKey", KeyStore.SecretKeyEntry(aesKey), aesKeyProtection())
    }

}