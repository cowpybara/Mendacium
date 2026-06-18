package com.example.mendacium.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mendacium.model.BandoGanador
import com.example.mendacium.model.MotorResolucion
import com.example.mendacium.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


enum class GamePhase {
    LOBBY,
    IMPOSTOR_NIGHT,
    DOCTOR_NIGHT,
    SEER_NIGHT,
    NIGHT_SUMMARY,
    DAY_DISCUSSION,
    DAY_VOTING,
    VERDICT,
    GAME_OVER
}

data class GameState(
    val players: List<Player> = emptyList(),
    val currentPhase: GamePhase = GamePhase.LOBBY,
    val impostorVictim: String? = null,
    val doctorProtected: String? = null,
    val seerInvestigated: String? = null,
    val lastEliminated: Player? = null,
    val winner: BandoGanador = BandoGanador.NINGUNO
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()


    fun iniciarPartida(initialPlayers: List<Player>) {
        _uiState.update { currentState ->
            currentState.copy(
                players = initialPlayers,
                currentPhase = GamePhase.IMPOSTOR_NIGHT,
                impostorVictim = null,
                doctorProtected = null,
                seerInvestigated = null,
                lastEliminated = null,
                winner = BandoGanador.NINGUNO
            )
        }
    }

    fun registrarAccionImpostor(victimName: String?) {
        _uiState.update { it.copy(impostorVictim = victimName, currentPhase = GamePhase.DOCTOR_NIGHT) }
    }

    fun registrarAccionMedico(protectedName: String?) {
        _uiState.update { it.copy(doctorProtected = protectedName, currentPhase = GamePhase.SEER_NIGHT) }
    }

    fun registrarAccionVidente(investigatedName: String?) {
        _uiState.update { it.copy(seerInvestigated = investigatedName) }
        resolverNoche()
    }

    private fun resolverNoche() {
        val state = _uiState.value

        val result = MotorResolucion.resolverNoche(
            jugadores = state.players,
            victimaImpostor = state.impostorVictim,
            protegidoMedico = state.doctorProtected
        )

        val currentWinner = MotorResolucion.evaluarVictoria(result.jugadoresActualizados)

        _uiState.update { currentState ->
            currentState.copy(
                players = result.jugadoresActualizados,
                lastEliminated = result.jugadorEliminado,
                winner = currentWinner,
                currentPhase = if (currentWinner != BandoGanador.NINGUNO) GamePhase.GAME_OVER else GamePhase.NIGHT_SUMMARY
            )
        }
    }

    fun cambiarFase(newPhase: GamePhase) {
        _uiState.update { it.copy(currentPhase = newPhase) }
    }
}