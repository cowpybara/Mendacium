package com.example.mendacium.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object SplashScreenRoute

@Serializable
object ModeSelectionRoute

@Serializable
object NameEntryScreenRoute

@Serializable
object JoinWithCodeScreenRoute

@Serializable
object ConfigurationScreenRoute

//pedir nombres locales
@Serializable
data class LocalPlayerNamesRoute(val totalPlayers: Int, val hostName: String)

@Serializable
object LobbyScreenRoute

@Serializable
object PassDeviceScreenRoute

@Serializable
object RoleRevealScreenRoute

//fase de noche
@Serializable
object VillagerSleepRoute

@Serializable
object ImpostorNightRoute

//pantallas de transición nocturna
@Serializable
data class ImpostorTransitionRoute(val playerName: String)

@Serializable
data class DoctorTransitionRoute(val playerName: String)

@Serializable
data class SeerTransitionRoute(val playerName: String)

@Serializable
object DoctorNightRoute

@Serializable
object SeerNightRoute

//modo en linea: pantalla de espera nocturna (aldeanos y roles que ya actuaron)
@Serializable
object WaitingNightRoute

@Serializable
object SeerRevealRoute

@Serializable
object NightSummaryRoute

//fase de dia
@Serializable
object DiscussionRoute

@Serializable
object VotingRoute

@Serializable
object VerdictRoute