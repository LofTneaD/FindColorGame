package com.example.findcolorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.findcolorgame.ui.components.ColorGrid
import com.example.findcolorgame.viewmodel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onGameOver: () -> Unit
) {
    val state = viewModel.state

    LaunchedEffect(state.gameOver) {
        if (state.gameOver) onGameOver()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Заголовок с информацией по режиму
        when (state.mode) {
            GameMode.CLASSIC -> {
                // Показать уровень и жизни
                Text(text = "Уровень: ${state.level}", style = MaterialTheme.typography.headlineSmall)
                Text(text = "Жизни: ${state.lives}", style = MaterialTheme.typography.titleMedium)
            }
            GameMode.TIME_ATTACK -> {
                // Показать таймер и уровень
                Text(text = "Время: ${state.timeLeft} с", style = MaterialTheme.typography.headlineSmall, color = Color.Red)
                Text(text = "Уровень: ${state.level}", style = MaterialTheme.typography.titleMedium)
            }
            GameMode.SURVIVAL -> {
                // Показать только уровень (жизни = 1)
                Text(text = "Уровень: ${state.level}", style = MaterialTheme.typography.headlineSmall)
                Text(text = "Ошибок не допускается!", style = MaterialTheme.typography.titleMedium, color = Color.Red)
            }
        }

        Spacer(Modifier.height(24.dp))

        ColorGrid(
            gridSize = state.gridSize,
            correctIndex = state.correctIndex,
            baseColor = state.baseColor,
            specialColor = state.specialColor,
            onCellClick = viewModel::onCellClick
        )
    }
}