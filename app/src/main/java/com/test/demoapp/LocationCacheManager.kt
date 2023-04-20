package com.test.demoapp

import android.content.Context
import android.location.Location
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class LocationCacheManager(val context: Context) : LocationCache {
    override suspend fun saveLocation(location: Location) {
        context.dataStore.edit { preferences ->
            preferences[USER_LOCATION_LAT] = location.latitude
            preferences[USER_LOCATION_LON] = location.longitude
            preferences[USER_LOCATION_ALTITUDE] = location.altitude
        }
    }

    override fun getLocation(): Flow<MyLocation> {
        return context.dataStore.data
            .catch { exception->
                if(exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
            MyLocation(
                longitude = preferences[USER_LOCATION_LON] ?: 0.0,
                latitude = preferences[USER_LOCATION_LAT] ?: 0.0,
                altitude = preferences[USER_LOCATION_ALTITUDE] ?: 0.0
            )

        }
    }
}