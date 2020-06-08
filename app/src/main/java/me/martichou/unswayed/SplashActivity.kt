package me.martichou.unswayed

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var mDelayHandler: Handler

    private val mRunnable = {
        if (!isFinishing) {
            val intent = Intent(this, MainActivity::class.java)
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