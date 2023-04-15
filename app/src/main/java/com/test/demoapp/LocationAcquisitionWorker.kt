package com.test.demoapp

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters


class LocationAcquisitionWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {
    override fun doWork(): Result {

        startLocationService()
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        stopLocationService()
    }

    private fun startLocationService() {
        Intent(applicationContext, LocationAcquisitionService::class.java).apply {
            action = LocationAcquisitionService.ACTION_START
            context.startService(this)
        }
    }

    private fun stopLocationService() {
        Intent(applicationContext, LocationAcquisitionService::class.java).apply {
            action = LocationAcquisitionService.ACTION_STOP
            context.startService(this)
        }
    }
}