package com.example.findcolorgame.ui.screens

import GameMode
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModeSelectScreen(
    onModeSelected: (GameMode) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Выберите режим", fontSize = 24.sp)

        Spacer(Modifier.height(24.dp))

        Button(onClick = { Log.d("ModeSelect", "Selected mode: CLASSIC"); onModeSelected(GameMode.CLASSIC) }) {

            Text("Классический")
        }

        Button(onClick = { onModeSelected(GameMode.TIME_ATTACK) }) {
            Text("На время")
        }

        Button(onClick = { onModeSelected(GameMode.SURVIVAL) }) {
            Text("Выживание")
        }
    }
}