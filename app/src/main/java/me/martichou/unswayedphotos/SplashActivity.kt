package me.martichou.unswayedphotos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import me.martichou.unswayedphotos.di.Injectable
import me.martichou.unswayedphotos.ui.AuthActivity
import me.martichou.unswayedphotos.ui.MainActivity
import me.martichou.unswayedphotos.util.TokenManager
import javax.inject.Inject

class SplashActivity : Activity(), Injectable {

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var mDelayHandler: Handler

    private val mRunnable = {
        if (!isFinishing) {
            val intent = if (tokenManager.token?.accessToken != null) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, AuthActivity::class.java)
            }
            startActivityForResult(intent, 0).also {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelayHandler = Handler()
    }

    override fun onPause() {
        super.onPause()
        mDelayHandler.removeCallbacks(mRunnable)
    }

    override fun onResume() {
        super.onResume()
        mDelayHandler.post(mRunnable)
    }

}