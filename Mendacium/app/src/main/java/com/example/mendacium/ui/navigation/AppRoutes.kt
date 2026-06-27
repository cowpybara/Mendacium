package com.example.mendacium.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object SplashScreenRoute

@Serializable
object NameEntryScreenRoute

@Serializable
object JoinWithCodeScreenRoute

@Serializable
object ConfigurationScreenRoute

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

//pantallas de transición nocturna (pasan el telefono entre roles sin revelar quien es quien)
@Serializable
object DoctorTransitionRoute

@Serializable
object SeerTransitionRoute

@Serializable
object DoctorNightRoute

@Serializable
object SeerNightRoute

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