package me.martichou.unswayedphotos.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import me.martichou.unswayedphotos.data.Result
import me.martichou.unswayedphotos.data.api.AuthService
import me.martichou.unswayedphotos.data.model.api.CredentialsData
import me.martichou.unswayedphotos.util.aesKeyProtection
import me.martichou.unswayedphotos.util.generateAesKeyFromPassword
import java.security.KeyStore
import javax.inject.Inject

class PasswordViewModel @Inject constructor(
    private val authService: AuthService,
    private val keyStore: KeyStore
) : ViewModel() {

    fun processConnection(data: CredentialsData) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            emit(Result.success(authService.auth(data)))
        } catch (exception: Exception) {
            emit(Result.error(data = null, message = exception.message ?: "An error occured"))
        }
    }

    suspend fun generateAndSaveAes(emailEE: ByteArray, pswdEE: String) {
        val aesKey = generateAesKeyFromPassword(pswdEE.toCharArray(), emailEE, 75666)
        keyStore.setEntry("aesKey", KeyStore.SecretKeyEntry(aesKey), aesKeyProtection())
    }

}