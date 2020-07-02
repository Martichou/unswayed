package me.martichou.unswayedphotos.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.martichou.unswayedphotos.SplashActivity
import me.martichou.unswayedphotos.ui.AuthActivity
import me.martichou.unswayedphotos.ui.MainActivity

@Suppress("unused")
@Module
abstract class ActivitiesModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity
}