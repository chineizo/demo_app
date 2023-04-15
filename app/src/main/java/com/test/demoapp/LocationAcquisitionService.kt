package com.test.demoapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


class LocationAcquisitionService : Service() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var locationClient: LocationClient
    private lateinit var mainHandler: Handler
    override fun onCreate() {
        super.onCreate()
        Timber.i("LocationAcquisitionService-->onCreate()")
        locationClient = LocationClientImpl(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )

        mainHandler = Handler(mainLooper)
    }

    override fun onBind(intent: Intent?): IBinder? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("LocationAcquisitionService-->onStart Comment with intent action = ${intent?.action}")
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        Timber.i("LocationAcquisitionService-->start()")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("location", "Location", NotificationManager.IMPORTANCE_LOW)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking Location")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient.getLocationUpdates(10000L)
            .catch { e -> Timber.e(e) }
            .onEach { location ->
                val lat = location.latitude
                val lon = location.latitude
                val alt = location.altitude
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $lon, $alt metres)"
                )
                Timber.i("LocationAcquisitionService--> Location: ($lat, $lon, $alt metres)")

                mainHandler.post(Runnable {
                    Toast.makeText(this, "Location: ($lat, $lon, $alt metres)", Toast.LENGTH_LONG)
                        .show()
                })



                notificationManager.notify(1, updatedNotification.build())
                stop()
            }
            .launchIn(scope)

        startForeground(1, notification.build())

    }

    private fun stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Timber.i("LocationAcquisitionService--> stop () option 1")
            stopForeground(STOP_FOREGROUND_DETACH)
        } else {
            Timber.i("LocationAcquisitionService--> stop () option 2")
            stopForeground(true)
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("LocationAcquisitionService--> onDestroy()")
        job.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}