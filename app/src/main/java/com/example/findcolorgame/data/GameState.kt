package com.example.findcolorgame.data

import GameMode
import androidx.compose.ui.graphics.Color

data class GameState(
    val mode: GameMode,
    val level: Int = 1,
    val lives: Int = 0,
    val timeLeft: Int = 0,
    val gridSize: Int = 2,
    val correctIndex: Int = 0,
    val baseColor: Color = Color.Gray,
    val specialColor: Color = Color.LightGray,
    val gameOver: Boolean = false
)