package com.example.findcolorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.findcolorgame.viewmodel.GameViewModel

@Composable
fun EnterNameScreen(
    onContinue: () -> Unit,
    viewModel: GameViewModel
) {
    val playerName = viewModel.playerName

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Введите имя игрока",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = playerName,
            onValueChange = { viewModel.setPlayerName(it) },
            singleLine = true,
            label = { Text("Имя") }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onContinue,
            enabled = playerName.isNotBlank()
        ) {
            Text("Продолжить")
        }
    }
}