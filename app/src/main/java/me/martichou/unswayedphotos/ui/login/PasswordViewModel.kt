package me.martichou.unswayedphotos.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import me.martichou.unswayedphotos.data.Result
import me.martichou.unswayedphotos.data.api.AuthService
import me.martichou.unswayedphotos.data.model.api.CredentialsData
import javax.inject.Inject

class PasswordViewModel @Inject constructor(val authService: AuthService) : ViewModel() {

    fun processConnection(data: CredentialsData) = liveData(Dispatchers.IO) {
        emit(Result.loading(data = null))
        try {
            val response = authService.auth(data)
            emit(Result.success(response))
        } catch (exception: Exception) {
            emit(Result.error(data = null, message = exception.message ?: "An error occured"))
        }
    }

}