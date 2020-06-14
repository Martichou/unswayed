package me.martichou.unswayed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import me.martichou.unswayed.utils.TokenManager

class SplashActivity : AppCompatActivity() {

    private lateinit var mDelayHandler: Handler
    private lateinit var tokenManager: TokenManager

    private val mRunnable = {
        if (!isFinishing) {
            val intent = if (tokenManager.token.accessToken != null) {
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
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                "prefs",
                Context.MODE_PRIVATE
            )
        )
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