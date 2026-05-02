package com.example.mendacium.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mendacium.model.GameConfiguration
import com.example.mendacium.ui.screen.ConfigurationScreen
import com.example.mendacium.ui.screen.LobbyScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ConfigurationScreenRoute,
        modifier = modifier
    ) {
        composable<ConfigurationScreenRoute> {
            ConfigurationScreen (
                onNavigateToLobby = { config ->
                    navController.navigate(
                        LobbyScreenRoute(
                            totalPlayers = config.totalPlayers,
                            impostors = config.impostorCount,
                            doctors = config.doctorCount,
                            seers = config.seerCount
                        )
                    )
                }
            )
        }

        composable<LobbyScreenRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            //Extrae los datos que viajaron en la ruta
            val routeData = backStackEntry.toRoute<LobbyScreenRoute>()
            LobbyScreen(
                totalPlayers = routeData.totalPlayers,
                impostors = routeData.impostors,
                doctors = routeData.doctors,
                seers = routeData.seers,
                onBack = {
                    navController.navigate(ConfigurationScreenRoute) {
                        popUpTo(LobbyScreenRoute) {
                            inclusive = true
                        }
                    }
                }
            )

        }
    }
}