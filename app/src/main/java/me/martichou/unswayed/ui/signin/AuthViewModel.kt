package me.martichou.unswayed.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import me.martichou.unswayed.models.LoginData
import me.martichou.unswayed.network.AuthService
import me.martichou.unswayed.utils.Resource

class AuthViewModel(private val authService: AuthService) : ViewModel() {

    fun perform(data: LoginData) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = authService.auth(data)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}