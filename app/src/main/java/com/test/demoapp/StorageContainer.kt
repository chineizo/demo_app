package com.test.demoapp

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.preferencesDataStore


val Context.dataStore by preferencesDataStore("location_store")
val USER_LOCATION_LAT = doublePreferencesKey("user_location_lat")
val USER_LOCATION_LON = doublePreferencesKey("user_location_lon")
val USER_LOCATION_ALTITUDE = doublePreferencesKey("user_location_alt")