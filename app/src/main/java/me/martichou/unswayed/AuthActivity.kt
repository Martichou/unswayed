package me.martichou.unswayed

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import me.martichou.unswayed.databinding.AuthActivityBinding
import me.martichou.unswayed.databinding.MainActivityBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: AuthActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        binding = DataBindingUtil.setContentView(this, R.layout.auth_activity)
        navController = Navigation.findNavController(this, R.id.auth_nav)
    }

}