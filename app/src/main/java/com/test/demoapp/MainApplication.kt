package com.test.demoapp

import android.app.Application
import androidx.work.PeriodicWorkRequest
import timber.log.Timber
import timber.log.Timber.Forest.plant


class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
    }
}