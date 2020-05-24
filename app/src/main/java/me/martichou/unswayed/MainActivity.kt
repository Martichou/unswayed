package me.martichou.unswayed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import me.martichou.unswayed.databinding.MainActivityBinding
import me.martichou.unswayed.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
        navController = Navigation.findNavController(this, R.id.main_nav)
    }
}