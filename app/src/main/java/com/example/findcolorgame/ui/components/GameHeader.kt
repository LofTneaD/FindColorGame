package com.example.findcolorgame.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.findcolorgame.data.GameState

@Composable
fun GameHeader(state: GameState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Уровень: ${state.level}")

        when (state.mode) {
            GameMode.CLASSIC, GameMode.SURVIVAL ->
                Text("❤️ ${state.lives}")

            GameMode.TIME_ATTACK ->
                Text("⏱ ${state.timeLeft}s")
        }
    }
}