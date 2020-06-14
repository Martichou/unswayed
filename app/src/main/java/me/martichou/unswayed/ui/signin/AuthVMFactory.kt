package me.martichou.unswayed.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.martichou.unswayed.network.AuthService

class AuthVMFactory(private val authService: AuthService) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authService) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}