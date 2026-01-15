package com.example.findcolorgame.navigation

import GameMode
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.findcolorgame.ui.screens.GameScreen
import com.example.findcolorgame.viewmodel.GameViewModel

@Composable
fun GameRoute(
    mode: GameMode,
    navController: NavController,
    viewModel: GameViewModel
) {
    LaunchedEffect(mode) {
        Log.d("GameRoute", "Starting game with mode: $mode")
        viewModel.startGame(mode)
    }

    LaunchedEffect(viewModel.state.gameOver) {
        if (viewModel.state.gameOver) {
            navController.navigate(Screen.GameOver.route)
        }
    }

    if (!viewModel.state.gameOver) {
        GameScreen(viewModel = viewModel, onGameOver = {})
    }
}