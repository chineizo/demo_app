package com.test.demoapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber


class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.i("BootCompleteReceiver ---> in Action")
        launchBootCompleteService(context)
    }

    private fun launchBootCompleteService(context: Context) {
        Intent(context, BootCompleteService::class.java).apply {
            action = BootCompleteService.ACTION_START
            context.startService(this)
        }
    }
}