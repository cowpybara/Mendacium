package com.example.mendacium.model

data class GameConfiguration(
    val totalPlayers: Int = 12,
    val impostorCount: Int = 2,
    val doctorCount: Int = 1,
    val seerCount: Int = 1
)