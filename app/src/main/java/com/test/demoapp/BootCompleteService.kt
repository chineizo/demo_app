package com.test.demoapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import timber.log.Timber
import java.util.concurrent.TimeUnit

class BootCompleteService : Service() {

    private val periodicWorkRequest =
        PeriodicWorkRequestBuilder<LocationAcquisitionWorker>(
            BuildConfig.location_acquisition_time_interval,
            TimeUnit.MILLISECONDS
        ).build()

    private fun enqueueLocationAcquisitionWorkManager() {
        WorkManager.getInstance(application).enqueue(periodicWorkRequest)
    }

    private fun cancelLocationAcquisitionWorkManager() {
        WorkManager.getInstance(application).cancelWorkById(periodicWorkRequest.id)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Timber.i("BootComplete Service ---> onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("BootCompleteService-->onStart Comment with intent action = ${intent?.action}")
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        Timber.i("BootCompleteService-->start()")
        enqueueLocationAcquisitionWorkManager()
    }

    private fun stop() {
        Timber.i("BootCompleteService-->stop()")
        cancelLocationAcquisitionWorkManager()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}