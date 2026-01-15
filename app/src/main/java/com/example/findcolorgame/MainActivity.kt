package com.example.findcolorgame

import GameMode
import LeaderboardScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.findcolorgame.data.FirebaseRepository
import com.example.findcolorgame.data.GamePreferencesManager
import com.example.findcolorgame.navigation.GameOverRoute
import com.example.findcolorgame.navigation.GameRoute
import com.example.findcolorgame.ui.screens.MainMenuScreen
import com.example.findcolorgame.ui.screens.ModeSelectScreen
import com.example.findcolorgame.navigation.Screen
import com.example.findcolorgame.ui.screens.EnterNameScreen
import com.example.findcolorgame.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    class GameViewModelFactory(
        private val prefsManager: GamePreferencesManager,
        private val firebaseRepository: FirebaseRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameViewModel(
                    prefsManager = prefsManager,
                    firebaseRepository = firebaseRepository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val prefsManager = GamePreferencesManager(applicationContext)
            val firebaseRepository = remember { FirebaseRepository() }
            val viewModel: GameViewModel = viewModel(
                factory = GameViewModelFactory(prefsManager, firebaseRepository)
            )

            NavHost(
                navController = navController,
                startDestination = Screen.Menu.route
            ) {
                composable(Screen.Menu.route) {
                    MainMenuScreen(
                        onPlayClick = {
                            navController.navigate(Screen.EnterName.route)
                        },
                        onLeaderboardClick = {
                            navController.navigate(Screen.Leaderboard.route)
                        }
                    )
                }

                composable(Screen.Leaderboard.route) {
                    LeaderboardScreen(
                        repository = firebaseRepository,
                        currentPlayerName = viewModel.playerName,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Mode.route) {
                    ModeSelectScreen(
                        onModeSelected = { mode ->
                            navController.navigate(Screen.Game.createRoute(mode))
                        }
                    )
                }

                composable(Screen.EnterName.route) {
                    EnterNameScreen(
                        viewModel = viewModel,
                        onContinue = {
                            navController.navigate(Screen.Mode.route)
                        }
                    )
                }

                composable(
                    route = Screen.Game.route,
                    arguments = listOf(
                        navArgument("mode") {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->

                    val mode = GameMode.valueOf(
                        backStackEntry.arguments?.getString("mode")!!
                    )
                    Log.d("NavHost", "Received mode argument: $mode")

                    GameRoute(
                        mode = mode,
                        navController = navController,
                        viewModel = viewModel
                    )
                }

                composable(Screen.GameOver.route) {
                    GameOverRoute(
                        navController = navController,
                        viewModel = viewModel,
                        prefsManager = prefsManager
                    )
                }
            }
        }
    }
}