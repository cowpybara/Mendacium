package com.example.mendacium.ui.navigation

import androidx.compose.ui.semantics.Role
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
data class RoleRevealScreenRoute(
    val roleName: String
)