package me.martichou.unswayed

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import me.martichou.unswayed.databinding.AuthActivityBinding

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        DataBindingUtil.setContentView<AuthActivityBinding>(this, R.layout.auth_activity)
    }

}