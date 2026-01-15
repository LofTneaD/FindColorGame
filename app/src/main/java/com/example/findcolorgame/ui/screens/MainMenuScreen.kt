package com.example.findcolorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen(
    onPlayClick: () -> Unit,
    onLeaderboardClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Find the Color",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onPlayClick,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Начать игру")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onLeaderboardClick,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Таблица рекордов")
        }
    }
}