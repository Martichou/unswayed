package me.martichou.unswayedphotos.ui

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import me.martichou.unswayedphotos.R
import me.martichou.unswayedphotos.data.api.AuthService
import me.martichou.unswayedphotos.databinding.MainActivityBinding
import me.martichou.unswayedphotos.service.TestJob
import me.martichou.unswayedphotos.util.Permission
import me.martichou.unswayedphotos.util.Permission.askPermissions
import me.martichou.unswayedphotos.util.TokenManager
import timber.log.Timber
import javax.inject.Inject

class MainActivity : FragmentActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        navController = Navigation.findNavController(this@MainActivity, R.id.main_nav)
        binding.apply {
            hdl = this@MainActivity
            bottomNav.setupWithNavController(navController)
        }
        scheduleJob()
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

    private fun scheduleJob() {
        val componentName = ComponentName(this, TestJob::class.java)
        val info = JobInfo.Builder(191919, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
            .setPeriodic(DateUtils.MINUTE_IN_MILLIS * 15)
            .build()
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        if (jobScheduler.getPendingJob(191919) == info) {
            Timber.d("DBG: The job is already running")
            return
        }
        jobScheduler.schedule(info)
        Timber.d("DBG: Job scheduled")
    }

}