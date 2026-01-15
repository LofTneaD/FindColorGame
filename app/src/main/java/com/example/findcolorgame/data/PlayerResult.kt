package com.example.findcolorgame.data

data class PlayerResult(
    val name: String = "",
    val mode: String = "",
    val score: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)