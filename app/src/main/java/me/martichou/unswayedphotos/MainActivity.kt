package me.martichou.unswayedphotos

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import me.martichou.unswayedphotos.databinding.MainActivityBinding
import me.martichou.unswayedphotos.util.Permission
import me.martichou.unswayedphotos.util.Permission.askPermissions
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        navController = Navigation.findNavController(this@MainActivity, R.id.main_nav)
        binding.apply {
            hdl = this@MainActivity
            bottomNav.setupWithNavController(navController)
        }
    }

    override fun onStart() {
        super.onStart()
        Glide.with(this)
            .load(
                "https://eu.ui-avatars.com/api/?name=${sharedPreferences.getString(
                    "user_email", "Unswayed Photo"
                )}&rounded=true&size=128"
            ).diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(binding.profileImage.measuredWidth, binding.profileImage.measuredWidth)
            .thumbnail(0.1f)
            .into(binding.profileImage)
    }

    override fun onResume() {
        super.onResume()
        if (!Permission.checkPermissions(this)) {
            askPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            19 -> {
                if (grantResults.isNotEmpty() || !Permission.checkPermissions(this)) {
                    // TODO - Implement
                    Timber.d("DBG: Permission denied")
                }
            }
        }
    }

    fun View.openDialog() = navController.navigate(R.id.settings_dialog)

}