package com.example.mendacium.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.mendacium.ui.viewmodel.GameViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel()
) {
    val navController = rememberNavController()
    val gameState by viewModel.uiState.collectAsState()


    var gameConfiguration by remember { mutableStateOf(GameConfiguration()) }
    var preGamePlayers by remember { mutableStateOf<List<Player>>(emptyList()) }
    var hostPlayerName by remember { mutableStateOf("Jugador 1") }


    var currentRevealIndex by remember { mutableIntStateOf(0) }


    var dayNumber by remember { mutableIntStateOf(1) }
    var seerTarget by remember { mutableStateOf<Player?>(null) }
    var dayEliminatedPlayer by remember { mutableStateOf<Player?>(null) }


    val activePlayers = if (gameState.players.isNotEmpty()) gameState.players else preGamePlayers

    NavHost(
        navController = navController,
        startDestination = SplashScreenRoute,
        modifier = modifier
    ) {
        composable<SplashScreenRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            SplashScreen(onPlayClick = { navController.navigate(NameEntryScreenRoute) })
        }

        composable<NameEntryScreenRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
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

        composable<JoinWithCodeScreenRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
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
                    preGamePlayers = buildLobbyPlayers(config.totalPlayers, hostPlayerName)
                    currentRevealIndex = 0
                    navController.navigate(LobbyScreenRoute)
                }
            )
        }

        // lobby
        composable<LobbyScreenRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            LobbyScreen(
                totalPlayers = gameConfiguration.totalPlayers,
                players = activePlayers,
                onBack = {
                    preGamePlayers = emptyList()
                    viewModel.limpiarPartida()
                    navController.popBackStack()
                },
                onStartGame = {
                    val assignedPlayers = assignRoles(preGamePlayers, gameConfiguration)
                    viewModel.iniciarPartida(assignedPlayers) // <-- LE PASAMOS EL MANDO AL CEREBRO
                    currentRevealIndex = 0
                    navController.navigate(PassDeviceScreenRoute)
                }
            )
        }

        // revelacion de roles
        composable<PassDeviceScreenRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            val currentPlayer = remember { activePlayers.getOrNull(currentRevealIndex) }
            if (currentPlayer != null) {
                PassDeviceScreen(
                    playerName = currentPlayer.name,
                    turnNumber = currentRevealIndex + 1,
                    totalPlayers = activePlayers.size,
                    onContinue = { navController.navigate(RoleRevealScreenRoute) }
                )
            }
        }

        composable<RoleRevealScreenRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            val currentPlayer = remember { activePlayers.getOrNull(currentRevealIndex) }
            if (currentPlayer != null) {
                RoleRevealScreen(
                    playerName = currentPlayer.name,
                    role = currentPlayer.role,
                    onUnderstand = {
                        if (currentRevealIndex < activePlayers.lastIndex) {
                            currentRevealIndex += 1
                            navController.navigate(PassDeviceScreenRoute)
                        } else {
                            navController.navigate(VillagerSleepRoute)
                        }
                    }
                )
            }
        }

        // fase de noche
        composable<VillagerSleepRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            VillagerSleepScreen(
                dayNumber = dayNumber,
                onStartNight = { navController.navigate(ImpostorNightRoute) }
            )
        }

        composable<ImpostorNightRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            val impostorNames = activePlayers
                .filter { it.role == Role.Impostor }
                .mapTo(mutableSetOf()) { it.name }

            val attackingSideLabel = if (impostorNames.size == 1) impostorNames.first() else "Los impostores"

            ImpostorNightScreen(
                allPlayers = activePlayers,
                excludedPlayerNames = impostorNames,
                attackingSideLabel = attackingSideLabel,
                onConfirmAttack = { victim ->
                    viewModel.registrarAccionImpostor(victim.name) // Se lo decimos al ViewModel

                    val doctor = activePlayers.firstOrNull { it.isAlive && it.role == Role.Doctor }
                    if (doctor != null) {
                        navController.navigate(DoctorNightRoute)
                    } else {
                        viewModel.registrarAccionMedico(null)
                        val seer = activePlayers.firstOrNull { it.isAlive && it.role == Role.Vidente }
                        if (seer != null) {
                            navController.navigate(SeerNightRoute)
                        } else {
                            viewModel.registrarAccionVidente(null) // Esto dispara MotorResolucion internamente
                            navController.navigate(NightSummaryRoute)
                        }
                    }
                }
            )
        }

        composable<DoctorNightRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            DoctorNightScreen(
                allPlayers = activePlayers,
                onConfirmProtection = { protected ->
                    viewModel.registrarAccionMedico(protected.name) // Se lo decimos al ViewModel

                    val seer = activePlayers.firstOrNull { it.isAlive && it.role == Role.Vidente }
                    if (seer != null) {
                        navController.navigate(SeerNightRoute)
                    } else {
                        viewModel.registrarAccionVidente(null) // Resuelve la noche mágicamente
                        navController.navigate(NightSummaryRoute)
                    }
                }
            )
        }

        composable<SeerNightRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            val seer = activePlayers.firstOrNull { it.isAlive && it.role == Role.Vidente }
            if (seer != null) {
                SeerNightScreen(
                    allPlayers = activePlayers,
                    seerName = seer.name,
                    onConfirmInvestigation = { target ->
                        seerTarget = target
                        navController.navigate(SeerRevealRoute)
                    }
                )
            }
        }

        composable<SeerRevealRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            val target = seerTarget
            if (target != null) {
                SeerRevealScreen(
                    investigatedPlayer = target,
                    onContinue = {
                        viewModel.registrarAccionVidente(target.name) // Resuelve la noche mágicamente
                        navController.navigate(NightSummaryRoute)
                    }
                )
            }
        }

        composable<NightSummaryRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            NightSummaryScreen(
                eliminatedPlayerName = gameState.lastEliminated?.name, // El cerebro nos dice quién murió
                survivors = activePlayers,
                onContinue = {
                    navController.navigate(DiscussionRoute)
                }
            )
        }

        // fase de dia
        composable<DiscussionRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            DiscussionScreen(
                players = activePlayers,
                dayNumber = dayNumber,
                onProceedToVoting = { navController.navigate(VotingRoute) }
            )
        }

        composable<VotingRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            VotingScreen(
                players = activePlayers,
                dayNumber = dayNumber,
                eliminatedNight = gameState.lastEliminated?.name,
                onConfirmVote = { voted ->
                    dayEliminatedPlayer = voted
                    if (voted != null) {
                        viewModel.registrarVotacionDia(voted.name) // Matamos al que votaron
                    }
                    navController.navigate(VerdictRoute)
                }
            )
        }

        composable<VerdictRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            val aliveImpostors = activePlayers.count { it.isAlive && it.role == Role.Impostor }
            val aliveTown = activePlayers.count { it.isAlive && it.role != Role.Impostor }
            val isGameOver = aliveImpostors == 0 || aliveImpostors >= aliveTown

            VerdictScreen(
                eliminatedPlayer = dayEliminatedPlayer,
                allPlayers = activePlayers,
                dayNumber = dayNumber,
                isGameOver = isGameOver,
                townsfolkWon = aliveImpostors == 0,
                onNextNight = {
                    dayNumber += 1
                    seerTarget = null
                    dayEliminatedPlayer = null
                    navController.navigate(VillagerSleepRoute)
                },
                onNewGame = {
                    viewModel.limpiarPartida()
                    gameConfiguration = GameConfiguration()
                    preGamePlayers = emptyList()
                    currentRevealIndex = 0
                    dayNumber = 1
                    seerTarget = null
                    dayEliminatedPlayer = null
                    navController.navigate(ConfigurationScreenRoute) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
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