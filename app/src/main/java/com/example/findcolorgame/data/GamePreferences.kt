package com.example.findcolorgame.data

import androidx.datastore.preferences.core.intPreferencesKey

object GamePreferences {
    val CLASSIC_BEST = intPreferencesKey("classic_best")
    val TIME_BEST = intPreferencesKey("time_best")
    val SURVIVAL_BEST = intPreferencesKey("survival_best")
}