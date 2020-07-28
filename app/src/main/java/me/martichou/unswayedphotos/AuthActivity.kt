package me.martichou.unswayedphotos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import me.martichou.unswayedphotos.R
import me.martichou.unswayedphotos.databinding.AuthActivityBinding

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<AuthActivityBinding>(this, R.layout.auth_activity)
    }

}