package com.example.mendacium.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mendacium.model.BandoGanador
import com.example.mendacium.model.MotorResolucion
import com.example.mendacium.model.Player
import com.example.mendacium.network.ApiClient
import com.example.mendacium.network.dto.SalaDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

data class NetworkState(
    val cargando: Boolean = false,
    val error: String? = null,
    val sala: SalaDto? = null
)

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

    private val _networkState = MutableStateFlow(NetworkState())
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    fun crearSala(hostNombre: String, onExito: (codigo: String) -> Unit) {
        viewModelScope.launch {
            _networkState.value = NetworkState(cargando = true)
            try {
                val sala = ApiClient.api.crearSala(mapOf("hostNombre" to hostNombre))
                _networkState.value = NetworkState(sala = sala)
                onExito(sala.codigo)
            } catch (e: Exception) {
                _networkState.value = NetworkState(error = "No se pudo crear la sala: ${e.message}")
            }
        }
    }

    fun unirseASala(codigo: String, nombre: String, onExito: () -> Unit) {
        viewModelScope.launch {
            _networkState.value = NetworkState(cargando = true)
            try {
                ApiClient.api.unirse(codigo, mapOf("nombre" to nombre))
                val sala = ApiClient.api.obtenerSala(codigo)
                _networkState.value = NetworkState(sala = sala)
                onExito()
            } catch (e: Exception) {
                _networkState.value = NetworkState(error = "Código inválido o sala no disponible")
            }
        }
    }

    fun actualizarSala(codigo: String) {
        viewModelScope.launch {
            try {
                val sala = ApiClient.api.obtenerSala(codigo)
                _networkState.update { it.copy(sala = sala) }
            } catch (_: Exception) {}
        }
    }

    fun limpiarErrorRed() {
        _networkState.update { it.copy(error = null) }
    }

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
        _uiState.update { it.copy(impostorVictim = victimName) }
    }

    fun registrarAccionMedico(protectedName: String?) {
        _uiState.update { it.copy(doctorProtected = protectedName) }
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

    fun registrarVotacionDia(linchadoName: String) {
        _uiState.update { currentState ->
            val updatedPlayers = currentState.players.map {
                if (it.name == linchadoName) it.copy(isAlive = false) else it
            }

            val currentWinner = MotorResolucion.evaluarVictoria(updatedPlayers)

            currentState.copy(
                players = updatedPlayers,
                winner = currentWinner,
                currentPhase = if (currentWinner != BandoGanador.NINGUNO) GamePhase.GAME_OVER else GamePhase.VERDICT
            )
        }
    }

    fun limpiarPartida() {
        _uiState.value = GameState()
    }
}