package com.example.findcolorgame.data

import GameMode
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GamePreferencesManager(context: Context) {
    private val Context.dataStore by preferencesDataStore("game_prefs")
    private val dataStore = context.dataStore

    fun getBestScoreFlow(mode: GameMode): Flow<Int> = dataStore.data
        .map { prefs ->
            when (mode) {
                GameMode.CLASSIC -> prefs[GamePreferences.CLASSIC_BEST] ?: 0
                GameMode.TIME_ATTACK -> prefs[GamePreferences.TIME_BEST] ?: 0
                GameMode.SURVIVAL -> prefs[GamePreferences.SURVIVAL_BEST] ?: 0
            }
        }

    suspend fun saveBestScore(mode: GameMode, score: Int) {
        dataStore.edit { prefs ->
            val currentBest = when (mode) {
                GameMode.CLASSIC -> prefs[GamePreferences.CLASSIC_BEST] ?: 0
                GameMode.TIME_ATTACK -> prefs[GamePreferences.TIME_BEST] ?: 0
                GameMode.SURVIVAL -> prefs[GamePreferences.SURVIVAL_BEST] ?: 0
            }
            if (score > currentBest) {
                when (mode) {
                    GameMode.CLASSIC -> prefs[GamePreferences.CLASSIC_BEST] = score
                    GameMode.TIME_ATTACK -> prefs[GamePreferences.TIME_BEST] = score
                    GameMode.SURVIVAL -> prefs[GamePreferences.SURVIVAL_BEST] = score
                }
            }
        }
    }
}