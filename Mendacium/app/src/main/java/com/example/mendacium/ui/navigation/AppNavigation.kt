package com.example.mendacium.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mendacium.model.Player
import com.example.mendacium.model.IconType
import com.example.mendacium.model.Role
import com.example.mendacium.ui.screen.ConfigurationScreen
import com.example.mendacium.ui.screen.LobbyScreen
import com.example.mendacium.ui.screen.RoleRevealScreen
import com.example.mendacium.ui.screen.ImpostorNightScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // Jugadores de prueba para la fase de noche
    val dummyPlayers = listOf(
        Player("Mateo", "NIVEL 42", true, IconType.LISTO, isAlive = true),
        Player("Joshua", "NIVEL 18", false, IconType.NINGUNO, isAlive = true),
        Player("Marcos", "NIVEL 05", false, IconType.CONECTANDO, isAlive = true),
        Player("Juan", "NIVEL 99", false, IconType.ESTRELLA, isAlive = false) // Este será filtrado
    )

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
            val routeData = backStackEntry.toRoute<LobbyScreenRoute>()
            LobbyScreen(
                totalPlayers = routeData.totalPlayers,
                impostors = routeData.impostors,
                doctors = routeData.doctors,
                seers = routeData.seers,
                onBack = {
                    navController.popBackStack()
                },
                onStartGame = {
                    navController.navigate(
                        RoleRevealScreenRoute(
                            roleName = Role.Impostor.name
                        )
                    )
                }
            )
        }

        composable<RoleRevealScreenRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val routeData = backStackEntry.toRoute<RoleRevealScreenRoute>()

            val assignedRole = when (routeData.roleName) {
                "VIDENTE" -> Role.Vidente
                "IMPOSTOR" -> Role.Impostor
                "MEDICO" -> Role.Doctor
                else -> Role.Aldeano
            }

            RoleRevealScreen(
                role = assignedRole,
                onUnderstand = {
                    // Si el rol es Impostor, lo mandamos a la pantalla de ataque
                    if (assignedRole == Role.Impostor) {
                        navController.navigate(ImpostorNightRoute)
                    } else {
                        // Aquí iría la lógica para otros roles
                    }
                }
            )
        }


        composable<ImpostorNightRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            ImpostorNightScreen(
                allPlayers = dummyPlayers,
                currentImpostorName = "Mateo",
                onConfirmAttack = { victim ->
                    println("Ataque confirmado contra: ${victim.name}")
                }
            )
        }
    }
}