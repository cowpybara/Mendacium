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
import com.example.mendacium.model.GameMode
import com.example.mendacium.model.IconType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mendacium.model.GameConfiguration
import com.example.mendacium.model.Player
import com.example.mendacium.model.Role
import com.example.mendacium.ui.screen.ConfigurationScreen
import com.example.mendacium.ui.screen.DiscussionScreen
import com.example.mendacium.ui.screen.DoctorNightScreen
import com.example.mendacium.ui.screen.ImpostorNightScreen
import com.example.mendacium.ui.screen.JoinWithCodeScreen
import com.example.mendacium.ui.screen.LobbyScreen
import com.example.mendacium.ui.screen.ModeSelectionScreen
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
import com.example.mendacium.ui.screen.WaitingNightScreen
import com.example.mendacium.ui.theme.NightSurface
import com.example.mendacium.ui.theme.OnBackgroundMuted
import com.example.mendacium.ui.theme.PurpleAccent
import com.example.mendacium.ui.viewmodel.GamePhase
import com.example.mendacium.ui.viewmodel.GameViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel()
) {
    val navController = rememberNavController()
    val gameState by viewModel.uiState.collectAsState()
    val networkState by viewModel.networkState.collectAsState()

    var gameMode by remember { mutableStateOf(GameMode.LOCAL) }
    var gameConfiguration by remember { mutableStateOf(GameConfiguration()) }
    var preGamePlayers by remember { mutableStateOf<List<Player>>(emptyList()) }
    var hostPlayerName by remember { mutableStateOf("Jugador 1") }

    var currentRevealIndex by remember { mutableIntStateOf(0) }

    var dayNumber by remember { mutableIntStateOf(1) }
    var seerTarget by remember { mutableStateOf<Player?>(null) }
    var dayEliminatedPlayer by remember { mutableStateOf<Player?>(null) }

    val activePlayers = if (gameState.players.isNotEmpty()) gameState.players else preGamePlayers

    // ─── Navegación reactiva: SOLO en modo en línea, dirigida por el servidor ───
    LaunchedEffect(gameState.currentPhase, gameMode) {
        if (gameMode != GameMode.ONLINE) return@LaunchedEffect
        when (gameState.currentPhase) {
            GamePhase.ROLE_REVEAL -> navController.navigate(RoleRevealScreenRoute) { launchSingleTop = true }
            GamePhase.IMPOSTOR_NIGHT -> navController.navigate(ImpostorNightRoute) { launchSingleTop = true }
            GamePhase.DOCTOR_NIGHT -> navController.navigate(DoctorNightRoute) { launchSingleTop = true }
            GamePhase.SEER_NIGHT -> navController.navigate(SeerNightRoute) { launchSingleTop = true }
            GamePhase.SEER_REVEAL -> navController.navigate(SeerRevealRoute) { launchSingleTop = true }
            GamePhase.NIGHT_WAITING -> navController.navigate(WaitingNightRoute) { launchSingleTop = true }
            GamePhase.NIGHT_SUMMARY -> navController.navigate(NightSummaryRoute) { launchSingleTop = true }
            GamePhase.DAY_DISCUSSION -> navController.navigate(DiscussionRoute) { launchSingleTop = true }
            GamePhase.DAY_VOTING -> navController.navigate(VotingRoute) { launchSingleTop = true }
            GamePhase.VERDICT, GamePhase.GAME_OVER -> navController.navigate(VerdictRoute) { launchSingleTop = true }
            else -> {}
        }
    }

    NavHost(
        navController = navController,
        startDestination = SplashScreenRoute,
        modifier = modifier
    ) {
        composable<SplashScreenRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            SplashScreen(onPlayClick = { navController.navigate(ModeSelectionRoute) })
        }

        composable<ModeSelectionRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            ModeSelectionScreen(
                onSelectLocal = {
                    gameMode = GameMode.LOCAL
                    navController.navigate(NameEntryScreenRoute)
                },
                onSelectOnline = {
                    gameMode = GameMode.ONLINE
                    navController.navigate(NameEntryScreenRoute)
                }
            )
        }

        composable<NameEntryScreenRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            NameEntryScreen(
                mostrarUnirse = gameMode == GameMode.ONLINE,
                onJoinWithCode = { name ->
                    hostPlayerName = name
                    navController.navigate(JoinWithCodeScreenRoute)
                },
                onCreateGame = { name ->
                    hostPlayerName = name
                    if (gameMode == GameMode.ONLINE) {
                        viewModel.crearSala(name) {}
                    }
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
                    if (gameMode == GameMode.LOCAL) {
                        preGamePlayers = buildLobbyPlayers(config.totalPlayers, hostPlayerName)
                    }
                    currentRevealIndex = 0
                    navController.navigate(LobbyScreenRoute)
                }
            )
        }

        //lobby
        composable<LobbyScreenRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            val codigoSala = networkState.sala?.codigo

            if (gameMode == GameMode.ONLINE) {
                // Conecta el WebSocket (reemplaza el polling) cuando ya hay código
                LaunchedEffect(codigoSala) {
                    if (codigoSala != null) {
                        viewModel.iniciarConexionWebSocket(codigoSala, gameState.miNombre)
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
                roomCode = if (gameMode == GameMode.ONLINE) codigoSala else null,
                showStartButton = gameMode == GameMode.LOCAL || gameState.soyHost,
                minPlayersToStart = 4,
                onBack = {
                    preGamePlayers = emptyList()
                    viewModel.limpiarPartida()
                    navController.popBackStack()
                },
                onStartGame = {
                    if (gameMode == GameMode.LOCAL) {
                        val assignedPlayers = assignRoles(preGamePlayers, gameConfiguration)
                        viewModel.iniciarPartida(assignedPlayers)
                        currentRevealIndex = 0
                        navController.navigate(PassDeviceScreenRoute)
                    } else {
                        // El host envía la configuración; el servidor reparte los roles
                        // y la navegación reactiva lleva a cada quien a su pantalla.
                        viewModel.hostIniciarPartida(
                            gameConfiguration.impostorCount,
                            gameConfiguration.doctorCount,
                            gameConfiguration.seerCount
                        )
                    }
                }
            )
        }

        //revelacion de rol
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
            if (gameMode == GameMode.LOCAL) {
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
            } else {
                RoleRevealScreen(
                    playerName = gameState.miNombre,
                    role = gameState.miRol,
                    onUnderstand = { viewModel.confirmarRolVisto() }
                )
            }
        }

        //fase de noche (LOCAL)
        composable<VillagerSleepRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            VillagerSleepScreen(
                dayNumber = dayNumber,
                onStartNight = {
                    val impostor = activePlayers.firstOrNull { it.isAlive && it.role == Role.Impostor }
                        ?: activePlayers.first { it.role == Role.Impostor }
                    navController.navigate(ImpostorTransitionRoute(impostor.name))
                }
            )
        }

        //transiciones nocturnas (LOCAL)
        composable<ImpostorTransitionRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) { backStackEntry ->
            val route = backStackEntry.toRoute<ImpostorTransitionRoute>()
            NightRoleTransitionScreen(
                playerName = route.playerName,
                onContinue = { navController.navigate(ImpostorNightRoute) }
            )
        }

        composable<ImpostorNightRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            if (gameMode == GameMode.LOCAL) {
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
                            navController.navigate(DoctorTransitionRoute(doctor.name))
                        } else {
                            viewModel.registrarAccionMedico(null)
                            val seer = activePlayers.firstOrNull { it.isAlive && it.role == Role.Vidente }
                            if (seer != null) {
                                navController.navigate(SeerTransitionRoute(seer.name))
                            } else {
                                viewModel.registrarAccionVidente(null)
                                navController.navigate(NightSummaryRoute)
                            }
                        }
                    }
                )
            } else {
                // ONLINE: solo el impostor ve esta pantalla; envía su ataque por WebSocket
                val excluidos = (gameState.aliados + gameState.miNombre).toSet()
                val etiqueta = if (gameState.aliados.isEmpty()) gameState.miNombre else "Los impostores"
                ImpostorNightScreen(
                    allPlayers = activePlayers,
                    excludedPlayerNames = excluidos,
                    attackingSideLabel = etiqueta,
                    onConfirmAttack = { victim -> viewModel.enviarAtaqueImpostor(victim.name) }
                )
            }
        }

        composable<DoctorTransitionRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) { backStackEntry ->
            val route = backStackEntry.toRoute<DoctorTransitionRoute>()
            NightRoleTransitionScreen(
                playerName = route.playerName,
                onContinue = { navController.navigate(DoctorNightRoute) }
            )
        }

        composable<DoctorNightRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            if (gameMode == GameMode.LOCAL) {
                DoctorNightScreen(
                    allPlayers = activePlayers,
                    onConfirmProtection = { protected ->
                        viewModel.registrarAccionMedico(protected.name)

                        val seer = activePlayers.firstOrNull { it.isAlive && it.role == Role.Vidente }
                        if (seer != null) {
                            navController.navigate(SeerTransitionRoute(seer.name))
                        } else {
                            viewModel.registrarAccionVidente(null)
                            navController.navigate(NightSummaryRoute)
                        }
                    }
                )
            } else {
                DoctorNightScreen(
                    allPlayers = activePlayers,
                    onConfirmProtection = { protected -> viewModel.enviarProteccion(protected.name) }
                )
            }
        }

        composable<SeerTransitionRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) { backStackEntry ->
            val route = backStackEntry.toRoute<SeerTransitionRoute>()
            NightRoleTransitionScreen(
                playerName = route.playerName,
                onContinue = { navController.navigate(SeerNightRoute) }
            )
        }

        composable<SeerNightRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            if (gameMode == GameMode.LOCAL) {
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
            } else {
                SeerNightScreen(
                    allPlayers = activePlayers,
                    seerName = gameState.miNombre,
                    onConfirmInvestigation = { target -> viewModel.enviarInvestigacion(target.name) }
                )
            }
        }

        composable<SeerRevealRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            if (gameMode == GameMode.LOCAL) {
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
            } else {
                val nombre = gameState.seerInvestigated
                val esMalo = gameState.seerRevealEsMalo
                if (nombre != null && esMalo != null) {
                    val investigado = Player(
                        name = nombre,
                        levelAndStatus = "",
                        iconType = IconType.NINGUNO,
                        role = if (esMalo) Role.Impostor else Role.Aldeano
                    )
                    SeerRevealScreen(
                        investigatedPlayer = investigado,
                        onContinue = { viewModel.cambiarFase(GamePhase.NIGHT_WAITING) }
                    )
                }
            }
        }

        //online: pantalla de espera nocturna
        composable<WaitingNightRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            WaitingNightScreen()
        }

        composable<NightSummaryRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            NightSummaryScreen(
                eliminatedPlayerName = gameState.lastEliminated?.name,
                survivors = gameState.players,
                isGameOver = gameState.winner != BandoGanador.NINGUNO,
                onContinue = {
                    if (gameMode == GameMode.LOCAL) {
                        if (gameState.winner != BandoGanador.NINGUNO) {
                            navController.navigate(VerdictRoute) {
                                popUpTo(NightSummaryRoute) { inclusive = true }
                            }
                        } else {
                            navController.navigate(DiscussionRoute)
                        }
                    } else {
                        // El host hace avanzar a todos a la discusión
                        if (gameState.soyHost) viewModel.hostContinuarDia()
                    }
                }
            )
        }

        //fase de dia
        composable<DiscussionRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            DiscussionScreen(
                players = activePlayers,
                dayNumber = if (gameMode == GameMode.ONLINE) gameState.numeroDia else dayNumber,
                onProceedToVoting = {
                    if (gameMode == GameMode.LOCAL) {
                        navController.navigate(VotingRoute)
                    } else {
                        if (gameState.soyHost) viewModel.hostIniciarVotacion()
                    }
                }
            )
        }

        composable<VotingRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            VotingScreen(
                players = activePlayers,
                dayNumber = if (gameMode == GameMode.ONLINE) gameState.numeroDia else dayNumber,
                eliminatedNight = gameState.lastEliminated?.name,
                onConfirmVote = { voted ->
                    if (gameMode == GameMode.LOCAL) {
                        dayEliminatedPlayer = voted
                        if (voted != null) {
                            viewModel.registrarVotacionDia(voted.name)
                        }
                        navController.navigate(VerdictRoute)
                    } else {
                        // Envía el voto; el servidor resuelve cuando todos votan
                        viewModel.enviarVotoOnline(voted?.name)
                    }
                }
            )
        }

        composable<VerdictRoute>(enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
            val isGameOver = gameState.winner != BandoGanador.NINGUNO
            val townsfolkWon = gameState.winner == BandoGanador.BUENOS
            val eliminado = if (gameMode == GameMode.LOCAL) dayEliminatedPlayer else gameState.lastEliminated

            VerdictScreen(
                eliminatedPlayer = eliminado,
                allPlayers = gameState.players,
                dayNumber = if (gameMode == GameMode.ONLINE) gameState.numeroDia else dayNumber,
                isGameOver = isGameOver,
                townsfolkWon = townsfolkWon,
                onNextNight = {
                    if (gameMode == GameMode.LOCAL) {
                        dayNumber += 1
                        seerTarget = null
                        dayEliminatedPlayer = null
                        navController.navigate(VillagerSleepRoute)
                    } else {
                        if (gameState.soyHost) viewModel.hostSiguienteNoche()
                    }
                },
                onNewGame = {
                    viewModel.limpiarPartida()
                    gameConfiguration = GameConfiguration()
                    preGamePlayers = emptyList()
                    currentRevealIndex = 0
                    dayNumber = 1
                    seerTarget = null
                    dayEliminatedPlayer = null
                    navController.navigate(ModeSelectionRoute) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }
    }
}

//pantalla de transición nocturna (modo LOCAL — pasar el teléfono)
// emoji y color neutros para que nadie pueda deducir el rol por la pantalla.
@Composable
private fun NightRoleTransitionScreen(
    playerName: String,
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
            text = "PASA EL TELEFONO",
            color = PurpleAccent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .size(110.dp)
                .background(PurpleAccent.copy(alpha = 0.10f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🌙", fontSize = 52.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = playerName,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Los demás, cierren los ojos.",
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
            colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "SOY $playerName, CONTINUAR",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
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
