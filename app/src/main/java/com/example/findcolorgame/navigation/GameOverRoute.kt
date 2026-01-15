package com.example.findcolorgame.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.findcolorgame.data.GamePreferencesManager
import com.example.findcolorgame.ui.screens.GameOverScreen
import com.example.findcolorgame.viewmodel.GameViewModel

@Composable
fun GameOverRoute(
    navController: NavController,
    viewModel: GameViewModel,
    prefsManager: GamePreferencesManager
) {

    GameOverScreen(
        state = viewModel.state,
        onRestart = {
            viewModel.startGame(viewModel.state.mode)

            navController.navigate(Screen.Game.createRoute(viewModel.state.mode)) {
                // Очистить стек до экрана Game с нужным режимом
                popUpTo(Screen.Game.createRoute(viewModel.state.mode)) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        },
        onExit = {
            navController.popBackStack(Screen.Menu.route, inclusive = false)
        }
    )
}