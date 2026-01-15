package com.example.findcolorgame.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorGrid(
    gridSize: Int,
    correctIndex: Int,
    baseColor: Color,
    specialColor: Color,
    onCellClick: (Int) -> Unit
) {
    val total = gridSize * gridSize

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridSize),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(total) { index ->
            ColorCell(
                color = if (index == correctIndex) specialColor else baseColor,
                onClick = { onCellClick(index) }
            )
        }
    }
}

@Composable
fun ColorCell(
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .background(color, RoundedCornerShape(8.dp))
            .clickable { onClick() }
    )
}