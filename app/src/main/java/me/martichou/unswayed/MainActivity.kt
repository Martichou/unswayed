package me.martichou.unswayed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import me.martichou.unswayed.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        navController = Navigation.findNavController(this, R.id.main_nav)
        binding.bottomNav.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        Glide.with(this)
            .load("https://avatars1.githubusercontent.com/u/23138751?s=460&u=f5eefef76889be5eae5ba43b2941224b066edcea&v=4")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .override(binding.profileImage.measuredWidth, binding.profileImage.measuredWidth)
            .thumbnail(0.1f)
            .error(R.drawable.placeholder)
            .into(binding.profileImage)
    }
}