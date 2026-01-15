package com.example.findcolorgame.navigation

import GameMode

sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object EnterName : Screen("enter_name")
    object Mode : Screen("mode")
    object Game : Screen("game/{mode}") {
        fun createRoute(mode: GameMode) = "game/${mode.name}"
    }
    object GameOver : Screen("game_over")
    object Leaderboard : Screen("leaderboard")
}