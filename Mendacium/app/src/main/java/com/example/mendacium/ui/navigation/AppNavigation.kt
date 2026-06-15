package com.example.mendacium.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mendacium.model.GameConfiguration
import com.example.mendacium.model.IconType
import com.example.mendacium.model.Player
import com.example.mendacium.model.Role
import com.example.mendacium.ui.screen.ConfigurationScreen
import com.example.mendacium.ui.screen.ImpostorNightScreen
import com.example.mendacium.ui.screen.LobbyScreen
import com.example.mendacium.ui.screen.NameEntryScreen
import com.example.mendacium.ui.screen.NightSummaryScreen
import com.example.mendacium.ui.screen.RoleRevealScreen
import com.example.mendacium.ui.screen.SplashScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    var gameConfiguration by remember { mutableStateOf(GameConfiguration()) }
    var players by remember { mutableStateOf<List<Player>>(emptyList()) }
    var currentRevealIndex by remember { mutableIntStateOf(0) }
    var lastEliminatedPlayerName by remember { mutableStateOf<String?>(null) }
    var hostPlayerName by remember { mutableStateOf("Jugador 1") }

    NavHost(
        navController = navController,
        startDestination = SplashScreenRoute,
        modifier = modifier
    ) {
        composable<SplashScreenRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            SplashScreen(
                onPlayClick = { navController.navigate(NameEntryScreenRoute) }
            )
        }

        composable<NameEntryScreenRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            NameEntryScreen(
                onJoinWithCode = { name ->
                    hostPlayerName = name
                    // TODO: navegar a pantalla de código cuando esté implementada
                },
                onCreateGame = { name ->
                    hostPlayerName = name
                    navController.navigate(ConfigurationScreenRoute)
                }
            )
        }

        composable<ConfigurationScreenRoute> {
            ConfigurationScreen(
                onNavigateToLobby = { config ->
                    gameConfiguration = config
                    players = buildLobbyPlayers(config.totalPlayers, hostPlayerName)
                    currentRevealIndex = 0
                    lastEliminatedPlayerName = null
                    navController.navigate(LobbyScreenRoute)
                }
            )
        }

        composable<LobbyScreenRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            LobbyScreen(
                totalPlayers = gameConfiguration.totalPlayers,
                players = players,
                onBack = {
                    players = emptyList()
                    navController.popBackStack()
                },
                onStartGame = {
                    players = assignRoles(players, gameConfiguration)
                    currentRevealIndex = 0
                    navController.navigate(RoleRevealScreenRoute)
                }
            )
        }

        composable<RoleRevealScreenRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            val currentPlayer = players.getOrNull(currentRevealIndex)

            if (currentPlayer != null) {
                RoleRevealScreen(
                    playerName = currentPlayer.name,
                    role = currentPlayer.role,
                    onUnderstand = {
                        if (currentRevealIndex < players.lastIndex) {
                            currentRevealIndex += 1
                        } else {
                            navController.navigate(ImpostorNightRoute)
                        }
                    }
                )
            }
        }

        composable<ImpostorNightRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            val impostorNames = players
                .filter { it.role == Role.Impostor }
                .mapTo(mutableSetOf()) { it.name }

            val attackingSideLabel = if (impostorNames.size == 1) {
                impostorNames.first()
            } else {
                "Los impostores"
            }

            ImpostorNightScreen(
                allPlayers = players,
                excludedPlayerNames = impostorNames,
                attackingSideLabel = attackingSideLabel,
                onConfirmAttack = { victim ->
                    lastEliminatedPlayerName = victim.name
                    players = players.map { player ->
                        if (player.name == victim.name) {
                            player.copy(isAlive = false)
                        } else {
                            player
                        }
                    }
                    navController.navigate(NightSummaryRoute)
                }
            )
        }

        composable<NightSummaryRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            NightSummaryScreen(
                eliminatedPlayerName = lastEliminatedPlayerName,
                onReturnToStart = {
                    gameConfiguration = GameConfiguration()
                    players = emptyList()
                    currentRevealIndex = 0
                    lastEliminatedPlayerName = null
                    navController.navigate(ConfigurationScreenRoute) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

private fun buildLobbyPlayers(totalPlayers: Int, hostName: String = "Jugador 1"): List<Player> {
    return List(totalPlayers) { index ->
        val playerNumber = index + 1
        Player(
            name = if (index == 0) hostName else "Jugador $playerNumber",
            levelAndStatus = if (index == 0) "ANFITRIÓN · LISTO" else "LISTO",
            isHost = index == 0,
            iconType = if (index == 0) IconType.ESTRELLA else IconType.LISTO
        )
    }
}

private fun assignRoles(players: List<Player>, configuration: GameConfiguration): List<Player> {
    val roles = mutableListOf<Role>()

    repeat(configuration.impostorCount) {
        roles.add(Role.Impostor)
    }

    repeat(configuration.doctorCount) {
        roles.add(Role.Doctor)
    }

    repeat(configuration.seerCount) {
        roles.add(Role.Vidente)
    }

    val villagerCount = configuration.totalPlayers - roles.size
    repeat(villagerCount) {
        roles.add(Role.Aldeano)
    }

    val shuffledRoles = roles.shuffled()

    return players.mapIndexed { index, player ->
        player.copy(
            role = shuffledRoles[index],
            isAlive = true
        )
    }
}
