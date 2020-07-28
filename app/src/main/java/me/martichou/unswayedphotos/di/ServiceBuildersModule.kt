package me.martichou.unswayedphotos.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.martichou.unswayedphotos.service.MyService
import me.martichou.unswayedphotos.service.TestJob

@Suppress("unused")
@Module
abstract class ServiceBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMyService(): MyService

    @ContributesAndroidInjector
    abstract fun contributeTestJob(): TestJob
}