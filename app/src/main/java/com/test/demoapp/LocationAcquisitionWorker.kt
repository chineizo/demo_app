package com.test.demoapp

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


class LocationAcquisitionWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    private lateinit var job: CompletableJob
    private lateinit var scope: CoroutineScope
    private lateinit var locationClient: LocationClient
    private lateinit var mainHandler: Handler
    private lateinit var locationCacheManager: LocationCacheManager
    override fun doWork(): Result {
        Timber.i("LocationAcquisitionWorker--> doWork")
        job = SupervisorJob()
        scope = CoroutineScope(Dispatchers.IO + job)
        mainHandler = Handler(Looper.getMainLooper())
        locationCacheManager = LocationCacheManager(context)
        locationClient = LocationClientImpl(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        startLocationService()
        return Result.success()
    }



    private fun startLocationService() {
        locationClient.getLocationUpdates(GPS_UPDATE_INTERVAL)
            .catch { e -> Timber.e(e) }
            .onEach { location ->
                val lat = location.latitude
                val lon = location.latitude
                val alt = location.altitude
                Timber.i("LocationAcquisitionWorker--> Location: ($lat, $lon, $alt metres)")
                mainHandler.post {
                    Toast.makeText(
                        context.applicationContext,
                        "Location: ($lat, $lon, $alt meters)",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

                //TODO Location can be persisted using Jetpack DataStore or Room Database
                locationCacheManager.saveLocation(location)
                job.cancelChildren()
            }
            .launchIn(scope)
    }


    companion object {
        const val GPS_UPDATE_INTERVAL = 20000L //every 20 seconds
    }
}