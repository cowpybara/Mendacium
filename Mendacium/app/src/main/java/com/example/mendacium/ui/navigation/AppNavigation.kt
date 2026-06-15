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
import com.example.mendacium.ui.screen.DiscussionScreen
import com.example.mendacium.ui.screen.DoctorNightScreen
import com.example.mendacium.ui.screen.ImpostorNightScreen
import com.example.mendacium.ui.screen.JoinWithCodeScreen
import com.example.mendacium.ui.screen.LobbyScreen
import com.example.mendacium.ui.screen.NameEntryScreen
import com.example.mendacium.ui.screen.NightSummaryScreen
import com.example.mendacium.ui.screen.PassDeviceScreen
import com.example.mendacium.ui.screen.RoleRevealScreen
import com.example.mendacium.ui.screen.SeerNightScreen
import com.example.mendacium.ui.screen.SeerRevealScreen
import com.example.mendacium.ui.screen.SplashScreen
import com.example.mendacium.ui.screen.VerdictScreen
import com.example.mendacium.ui.screen.VillagerSleepScreen
import com.example.mendacium.ui.screen.VotingScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // Configuración y jugadores
    var gameConfiguration by remember { mutableStateOf(GameConfiguration()) }
    var players by remember { mutableStateOf<List<Player>>(emptyList()) }
    var hostPlayerName by remember { mutableStateOf("Jugador 1") }

    // Fase de revelación de roles
    var currentRevealIndex by remember { mutableIntStateOf(0) }

    // Estado de la noche
    var dayNumber by remember { mutableIntStateOf(1) }
    var nightVictimName by remember { mutableStateOf<String?>(null) }
    var doctorProtectedName by remember { mutableStateOf<String?>(null) }
    var seerTarget by remember { mutableStateOf<Player?>(null) }
    var actualEliminatedName by remember { mutableStateOf<String?>(null) }

    // Estado del día
    var dayEliminatedPlayer by remember { mutableStateOf<Player?>(null) }

    NavHost(
        navController = navController,
        startDestination = SplashScreenRoute,
        modifier = modifier
    ) {
        // ── ACCESO ──────────────────────────────────────────────────────────────

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
                    navController.navigate(JoinWithCodeScreenRoute)
                },
                onCreateGame = { name ->
                    hostPlayerName = name
                    navController.navigate(ConfigurationScreenRoute)
                }
            )
        }

        composable<JoinWithCodeScreenRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            JoinWithCodeScreen(
                playerName = hostPlayerName,
                onBack = { navController.popBackStack() },
                onEnterRoom = { /* TODO: backend */ }
            )
        }

        composable<ConfigurationScreenRoute> {
            ConfigurationScreen(
                onNavigateToLobby = { config ->
                    gameConfiguration = config
                    players = buildLobbyPlayers(config.totalPlayers, hostPlayerName)
                    currentRevealIndex = 0
                    navController.navigate(LobbyScreenRoute)
                }
            )
        }

        // ── LOBBY ───────────────────────────────────────────────────────────────

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
                    navController.navigate(PassDeviceScreenRoute)
                }
            )
        }

        // ── REVELACIÓN DE ROLES ─────────────────────────────────────────────────

        composable<PassDeviceScreenRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            // remember sin clave: captura el jugador al entrar y no reacciona a cambios
            val currentPlayer = remember { players.getOrNull(currentRevealIndex) }
            if (currentPlayer != null) {
                PassDeviceScreen(
                    playerName = currentPlayer.name,
                    turnNumber = currentRevealIndex + 1,
                    totalPlayers = players.size,
                    onContinue = { navController.navigate(RoleRevealScreenRoute) }
                )
            }
        }

        composable<RoleRevealScreenRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            // remember sin clave: congela el jugador al entrar; evita mostrar el
            // siguiente rol durante la animación de salida al incrementar el índice
            val currentPlayer = remember { players.getOrNull(currentRevealIndex) }
            if (currentPlayer != null) {
                RoleRevealScreen(
                    playerName = currentPlayer.name,
                    role = currentPlayer.role,
                    onUnderstand = {
                        if (currentRevealIndex < players.lastIndex) {
                            currentRevealIndex += 1
                            navController.navigate(PassDeviceScreenRoute)
                        } else {
                            navController.navigate(VillagerSleepRoute)
                        }
                    }
                )
            }
        }

        // ── FASE DE NOCHE ────────────────────────────────────────────────────────

        composable<VillagerSleepRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            VillagerSleepScreen(
                dayNumber = dayNumber,
                onStartNight = { navController.navigate(ImpostorNightRoute) }
            )
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
                    nightVictimName = victim.name
                    val doctor = players.firstOrNull { it.isAlive && it.role == Role.Doctor }
                    if (doctor != null) {
                        navController.navigate(DoctorNightRoute)
                    } else {
                        // Médico muerto: saltar directamente al Vidente o resolver noche
                        val seer = players.firstOrNull { it.isAlive && it.role == Role.Vidente }
                        if (seer != null) {
                            navController.navigate(SeerNightRoute)
                        } else {
                            resolveNight(
                                victim.name, null,
                                players,
                                onPlayersUpdated = { updatedPlayers, eliminated ->
                                    players = updatedPlayers
                                    actualEliminatedName = eliminated
                                }
                            )
                            navController.navigate(NightSummaryRoute)
                        }
                    }
                }
            )
        }

        composable<DoctorNightRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            DoctorNightScreen(
                allPlayers = players,
                onConfirmProtection = { protected ->
                    doctorProtectedName = protected.name
                    val seer = players.firstOrNull { it.isAlive && it.role == Role.Vidente }
                    if (seer != null) {
                        navController.navigate(SeerNightRoute)
                    } else {
                        resolveNight(
                            nightVictimName, doctorProtectedName,
                            players,
                            onPlayersUpdated = { updatedPlayers, eliminated ->
                                players = updatedPlayers
                                actualEliminatedName = eliminated
                            }
                        )
                        navController.navigate(NightSummaryRoute)
                    }
                }
            )
        }

        composable<SeerNightRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            val seer = players.firstOrNull { it.isAlive && it.role == Role.Vidente }
            if (seer != null) {
                SeerNightScreen(
                    allPlayers = players,
                    seerName = seer.name,
                    onConfirmInvestigation = { target ->
                        seerTarget = target
                        navController.navigate(SeerRevealRoute)
                    }
                )
            }
        }

        composable<SeerRevealRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            val target = seerTarget
            if (target != null) {
                SeerRevealScreen(
                    investigatedPlayer = target,
                    onContinue = {
                        resolveNight(
                            nightVictimName, doctorProtectedName,
                            players,
                            onPlayersUpdated = { updatedPlayers, eliminated ->
                                players = updatedPlayers
                                actualEliminatedName = eliminated
                            }
                        )
                        navController.navigate(NightSummaryRoute)
                    }
                )
            }
        }

        composable<NightSummaryRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            NightSummaryScreen(
                eliminatedPlayerName = actualEliminatedName,
                survivors = players,
                onContinue = {
                    navController.navigate(DiscussionRoute)
                }
            )
        }

        // ── FASE DE DÍA ──────────────────────────────────────────────────────────

        composable<DiscussionRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            DiscussionScreen(
                players = players,
                dayNumber = dayNumber,
                onProceedToVoting = { navController.navigate(VotingRoute) }
            )
        }

        composable<VotingRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            VotingScreen(
                players = players,
                dayNumber = dayNumber,
                eliminatedNight = actualEliminatedName,
                onConfirmVote = { voted ->
                    dayEliminatedPlayer = voted
                    if (voted != null) {
                        players = players.map { p ->
                            if (p.name == voted.name) p.copy(isAlive = false) else p
                        }
                    }
                    navController.navigate(VerdictRoute)
                }
            )
        }

        composable<VerdictRoute>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            val aliveImpostors = players.count { it.isAlive && it.role == Role.Impostor }
            val aliveTown = players.count { it.isAlive && it.role != Role.Impostor }
            val isGameOver = aliveImpostors == 0 || aliveImpostors >= aliveTown

            VerdictScreen(
                eliminatedPlayer = dayEliminatedPlayer,
                allPlayers = players,
                dayNumber = dayNumber,
                isGameOver = isGameOver,
                townsfolkWon = aliveImpostors == 0,
                onNextNight = {
                    // Limpiar estado nocturno y avanzar día
                    dayNumber += 1
                    nightVictimName = null
                    doctorProtectedName = null
                    seerTarget = null
                    actualEliminatedName = null
                    dayEliminatedPlayer = null
                    navController.navigate(VillagerSleepRoute)
                },
                onNewGame = {
                    // Reset completo
                    gameConfiguration = GameConfiguration()
                    players = emptyList()
                    currentRevealIndex = 0
                    dayNumber = 1
                    nightVictimName = null
                    doctorProtectedName = null
                    seerTarget = null
                    actualEliminatedName = null
                    dayEliminatedPlayer = null
                    navController.navigate(ConfigurationScreenRoute) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }
    }
}

// ── Helpers ──────────────────────────────────────────────────────────────────

private fun resolveNight(
    nightVictimName: String?,
    doctorProtectedName: String?,
    players: List<Player>,
    onPlayersUpdated: (List<Player>, String?) -> Unit
) {
    val actualEliminated = when {
        nightVictimName == null -> null
        nightVictimName == doctorProtectedName -> null
        else -> nightVictimName
    }
    val updatedPlayers = if (actualEliminated != null) {
        players.map { if (it.name == actualEliminated) it.copy(isAlive = false) else it }
    } else {
        players
    }
    onPlayersUpdated(updatedPlayers, actualEliminated)
}

private fun buildLobbyPlayers(totalPlayers: Int, hostName: String = "Jugador 1"): List<Player> {
    return List(totalPlayers) { index ->
        val playerNumber = index + 1
        Player(
            name = if (index == 0) hostName else "Jugador $playerNumber",
            levelAndStatus = if (index == 0) "ANFITRIÓN · LISTO" else "LISTO",
            isHost = index == 0,
            iconType = if (index == 0) IconType.ESTRELLA else IconType.LISTO,
            avatarColorIndex = index
        )
    }
}

private fun assignRoles(players: List<Player>, configuration: GameConfiguration): List<Player> {
    val roles = mutableListOf<Role>()
    repeat(configuration.impostorCount) { roles.add(Role.Impostor) }
    repeat(configuration.doctorCount) { roles.add(Role.Doctor) }
    repeat(configuration.seerCount) { roles.add(Role.Vidente) }
    val villagerCount = configuration.totalPlayers - roles.size
    repeat(villagerCount) { roles.add(Role.Aldeano) }
    val shuffledRoles = roles.shuffled()
    return players.mapIndexed { index, player ->
        player.copy(role = shuffledRoles[index], isAlive = true)
    }
}
