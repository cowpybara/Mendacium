package com.example.mendacium.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mendacium.model.Role
import com.example.mendacium.ui.screen.ConfigurationScreen
import com.example.mendacium.ui.screen.LobbyScreen
import com.example.mendacium.ui.screen.RoleRevealScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "configuracion") {
        composable("configuracion") {
            ConfigurationScreen(onNavigateToLobby = { navController.navigate("lobby") })
        }

        composable("lobby") {
            LobbyScreen(onStartGame = {
                navController.navigate("revelar_rol")
            })
        }

        composable("revelar_rol") {
            val rolesPosibles = listOf(
                Role.Vidente,
                Role.Impostor,
                Role.Doctor,
                Role.Aldeano
            )
            val rolAsignado = rolesPosibles.random() // <--- ESTO ELIGE AL AZAR

            RoleRevealScreen(role = rolAsignado, onUnderstand = {
                navController.navigate("configuracion")
            })
        }
    }
}