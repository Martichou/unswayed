package me.martichou.unswayedphotos.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.martichou.unswayedphotos.ui.home.HomeFragment
import me.martichou.unswayedphotos.ui.login.PasswordFragment
import me.martichou.unswayedphotos.ui.settings.SettingsDialog

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsDialog(): SettingsDialog

    @ContributesAndroidInjector
    abstract fun contributePasswordFragment(): PasswordFragment
}