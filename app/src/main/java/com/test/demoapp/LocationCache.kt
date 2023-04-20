package com.test.demoapp

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationCache {
    suspend fun saveLocation(location: Location)
    fun getLocation(): Flow<MyLocation>
}