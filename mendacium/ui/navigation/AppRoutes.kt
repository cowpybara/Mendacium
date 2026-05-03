package com.example.mendacium.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object ConfigurationScreenRoute

@Serializable
data class LobbyScreenRoute(
    val totalPlayers: Int,
    val impostors: Int,
    val doctors: Int,
    val seers: Int
)

@Serializable
object NightActionRoute // Nueva ruta para el avance