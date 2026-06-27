package com.example.mendacium.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.BandoGanador
import com.example.mendacium.model.IconType
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mendacium.model.GameConfiguration
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
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.NightSurface
import com.example.mendacium.ui.theme.OnBackgroundMuted
import com.example.mendacium.ui.theme.PurpleAccent
import com.example.mendacium.ui.theme.PurpleSurface
import com.example.mendacium.ui.viewmodel.GameViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel()
) {
    val navController = rememberNavController()
    val gameState by viewModel.uiState.collectAsState()
    val networkState by viewModel.networkState.collectAsState()

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
                    viewModel.crearSala(name) {}
                    navController.navigate(ConfigurationScreenRoute)
                }
            )
        }

        composable<JoinWithCodeScreenRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            JoinWithCodeScreen(
                playerName = hostPlayerName,
                onBack = { navController.popBackStack() },
                onEnterRoom = { codigo ->
                    viewModel.unirseASala(codigo, hostPlayerName) {
                        navController.navigate(LobbyScreenRoute)
                    }
                }
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

        //lobby
        composable<LobbyScreenRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            val codigoSala = networkState.sala?.codigo

            LaunchedEffect(codigoSala) {
                if (codigoSala != null) {
                    while (true) {
                        delay(3000)
                        viewModel.actualizarSala(codigoSala)
                    }
                }
            }

            val backendPlayers = networkState.sala?.jugadores?.mapIndexed { index, j ->
                Player(
                    name = j.nombre,
                    levelAndStatus = if (j.esHost) "ANFITRIÓN · LISTO" else "LISTO",
                    isHost = j.esHost,
                    iconType = if (j.esHost) IconType.ESTRELLA else IconType.LISTO,
                    avatarColorIndex = index % 12
                )
            } ?: emptyList()

            val displayPlayers = if (backendPlayers.isNotEmpty()) backendPlayers else activePlayers
            val displayTotal = if (backendPlayers.isNotEmpty()) backendPlayers.size else gameConfiguration.totalPlayers

            LobbyScreen(
                totalPlayers = displayTotal,
                players = displayPlayers,
                roomCode = codigoSala,
                onBack = {
                    preGamePlayers = emptyList()
                    viewModel.limpiarPartida()
                    navController.popBackStack()
                },
                onStartGame = {
                    val playersToAssign = if (backendPlayers.isNotEmpty()) backendPlayers else preGamePlayers
                    val assignedPlayers = assignRoles(playersToAssign, gameConfiguration)
                    viewModel.iniciarPartida(assignedPlayers)
                    currentRevealIndex = 0
                    navController.navigate(PassDeviceScreenRoute)
                }
            )
        }

        //revelacion de roled
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

        //fase de noche
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
                    viewModel.registrarAccionImpostor(victim.name)

                    val doctor = activePlayers.firstOrNull { it.isAlive && it.role == Role.Doctor }
                    if (doctor != null) {
                        // FIX 2: Pantalla de transición antes de mostrar la acción del Médico
                        navController.navigate(DoctorTransitionRoute)
                    } else {
                        viewModel.registrarAccionMedico(null)
                        val seer = activePlayers.firstOrNull { it.isAlive && it.role == Role.Vidente }
                        if (seer != null) {
                            navController.navigate(SeerTransitionRoute)
                        } else {
                            viewModel.registrarAccionVidente(null)
                            navController.navigate(NightSummaryRoute)
                        }
                    }
                }
            )
        }

        // FIX 2: Pantalla "Pasa el teléfono al Médico"
        composable<DoctorTransitionRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            NightRoleTransitionScreen(
                emoji = "💊",
                roleTitle = "MÉDICO",
                instruction = "El Médico debe tomar el teléfono.\nLos demás, cierren los ojos.",
                buttonText = "ESTOY LISTO",
                accentColor = Color(0xFF4ADE80),
                onContinue = { navController.navigate(DoctorNightRoute) }
            )
        }

        composable<DoctorNightRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            DoctorNightScreen(
                allPlayers = activePlayers,
                onConfirmProtection = { protected ->
                    viewModel.registrarAccionMedico(protected.name)

                    val seer = activePlayers.firstOrNull { it.isAlive && it.role == Role.Vidente }
                    if (seer != null) {
                        // FIX 2: Pantalla de transición antes de mostrar la acción de la Vidente
                        navController.navigate(SeerTransitionRoute)
                    } else {
                        viewModel.registrarAccionVidente(null)
                        navController.navigate(NightSummaryRoute)
                    }
                }
            )
        }

        // FIX 2: Pantalla "Pasa el teléfono a la Vidente"
        composable<SeerTransitionRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            NightRoleTransitionScreen(
                emoji = "🔮",
                roleTitle = "VIDENTE",
                instruction = "La Vidente debe tomar el teléfono.\nLos demás, cierren los ojos.",
                buttonText = "ESTOY LISTA",
                accentColor = Color(0xFFD8B4FE),
                onContinue = { navController.navigate(SeerNightRoute) }
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
                        viewModel.registrarAccionVidente(target.name)
                        navController.navigate(NightSummaryRoute)
                    }
                )
            }
        }

        // FIX 1: NightSummary verifica si el juego terminó antes de navegar
        composable<NightSummaryRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            NightSummaryScreen(
                eliminatedPlayerName = gameState.lastEliminated?.name,
                survivors = gameState.players,
                onContinue = {
                    // FIX 1: Si ya hay ganador (la noche fue decisiva), ir directo al veredicto
                    if (gameState.winner != BandoGanador.NINGUNO) {
                        navController.navigate(VerdictRoute) {
                            popUpTo(NightSummaryRoute) { inclusive = true }
                        }
                    } else {
                        navController.navigate(DiscussionRoute)
                    }
                }
            )
        }

        // ── FASE DE DÍA ──────────────────────────────────────────────────────
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
                        viewModel.registrarVotacionDia(voted.name)
                    }
                    navController.navigate(VerdictRoute)
                }
            )
        }

        // FIX 3: VerdictRoute usa gameState.winner en vez de calcular localmente
        composable<VerdictRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            val isGameOver = gameState.winner != BandoGanador.NINGUNO
            val townsfolkWon = gameState.winner == BandoGanador.BUENOS

            VerdictScreen(
                eliminatedPlayer = dayEliminatedPlayer,
                allPlayers = gameState.players,
                dayNumber = dayNumber,
                isGameOver = isGameOver,
                townsfolkWon = townsfolkWon,
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

// ── Pantalla de transición nocturna (usada entre roles) ────────────────────────
@Composable
private fun NightRoleTransitionScreen(
    emoji: String,
    roleTitle: String,
    instruction: String,
    buttonText: String,
    accentColor: Color,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NightSurface)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PASA EL TELÉFONO",
            color = accentColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .size(110.dp)
                .background(accentColor.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 52.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = roleTitle,
            color = accentColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = instruction,
            color = OnBackgroundMuted,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(56.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = buttonText,
                color = Color(0xFF0D0B14),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

// ── Helpers ──────────────────────────────────────────────────────────────────
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