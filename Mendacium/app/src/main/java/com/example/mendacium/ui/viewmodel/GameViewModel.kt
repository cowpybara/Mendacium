package com.example.mendacium.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mendacium.model.BandoGanador
import com.example.mendacium.model.IconType
import com.example.mendacium.model.MotorResolucion
import com.example.mendacium.model.Player
import com.example.mendacium.model.Role
import com.example.mendacium.ui.model.PayloadFaseDia
import com.example.mendacium.ui.model.PayloadFaseNoche
import com.example.mendacium.ui.model.PayloadFinPartida
import com.example.mendacium.ui.model.PayloadPartidaIniciada
import com.example.mendacium.ui.model.PayloadResultadoInvestigacion
import com.example.mendacium.ui.model.PayloadResultadoNoche
import com.example.mendacium.ui.model.PayloadResultadoVotacion
import com.example.mendacium.ui.model.Sala
import com.example.mendacium.ui.service.ApiResult
import com.example.mendacium.ui.service.ServiceLocator
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class GamePhase {
    LOBBY,
    ROLE_REVEAL,      // online: cada teléfono ve su propio rol
    IMPOSTOR_NIGHT,
    DOCTOR_NIGHT,
    SEER_NIGHT,
    SEER_REVEAL,      // online: resultado privado de la investigación
    NIGHT_WAITING,    // online: aldeanos y roles que ya actuaron esperan
    NIGHT_SUMMARY,
    DAY_DISCUSSION,
    DAY_VOTING,
    VERDICT,
    GAME_OVER
}

data class NetworkState(
    val cargando: Boolean = false,
    val error: String? = null,
    val sala: Sala? = null
)

data class GameState(
    val players: List<Player> = emptyList(),
    val currentPhase: GamePhase = GamePhase.LOBBY,
    val impostorVictim: String? = null,
    val doctorProtected: String? = null,
    val seerInvestigated: String? = null,
    val lastEliminated: Player? = null,
    val winner: BandoGanador = BandoGanador.NINGUNO,
    // ─── Campos del modo en línea (el modo local los ignora) ───
    val miRol: Role = Role.Aldeano,
    val miNombre: String = "",
    val soyHost: Boolean = false,
    val aliados: List<String> = emptyList(),
    val seerRevealEsMalo: Boolean? = null,
    val numeroDia: Int = 0
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private val _networkState = MutableStateFlow(NetworkState())
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    private val repository = ServiceLocator.salaRepository
    private val wsRepository = ServiceLocator.wsRepository
    private val gson = Gson()

    private var wsCodigo: String? = null

    // ───────────────────────────── REST (lobby) ─────────────────────────────

    fun crearSala(hostNombre: String, onExito: (codigo: String) -> Unit) {
        viewModelScope.launch {
            _networkState.value = NetworkState(cargando = true)
            when (val result = repository.crearSala(hostNombre)) {
                is ApiResult.Success -> {
                    _networkState.value = NetworkState(sala = result.data)
                    _uiState.update { it.copy(miNombre = hostNombre, soyHost = true) }
                    onExito(result.data.codigo)
                }
                is ApiResult.Error -> {
                    _networkState.value = NetworkState(error = result.message)
                }
            }
        }
    }

    fun unirseASala(codigo: String, nombre: String, onExito: () -> Unit) {
        if (_networkState.value.cargando) return // evita dobles toques mientras carga
        viewModelScope.launch {
            _networkState.value = NetworkState(cargando = true)
            when (val result = repository.unirseASala(codigo, nombre)) {
                is ApiResult.Success -> {
                    _networkState.value = NetworkState(sala = result.data)
                    _uiState.update { it.copy(miNombre = nombre, soyHost = false) }
                    onExito()
                }
                is ApiResult.Error -> {
                    _networkState.value = NetworkState(error = result.message)
                }
            }
        }
    }

    fun actualizarSala(codigo: String) {
        viewModelScope.launch {
            when (val result = repository.obtenerSala(codigo)) {
                is ApiResult.Success -> _networkState.update { it.copy(sala = result.data) }
                is ApiResult.Error -> {}
            }
        }
    }

    fun limpiarErrorRed() {
        _networkState.update { it.copy(error = null) }
    }

    // ───────────────────────────── WebSocket (modo en línea) ─────────────────────────────

    fun iniciarConexionWebSocket(codigo: String, nombre: String) {
        wsCodigo = codigo
        viewModelScope.launch {
            try {
                wsRepository.conectar()
                _uiState.update { it.copy(miNombre = nombre) }
                launch {
                    wsRepository.suscribirSala(codigo).collect { msg -> procesarMensajeSala(msg) }
                }
                launch {
                    wsRepository.suscribirJugador(nombre).collect { msg -> procesarMensajeJugador(msg) }
                }
            } catch (e: Exception) {
                _networkState.update { it.copy(error = "No se pudo conectar al servidor: ${e.message}") }
            }
        }
    }

    private fun procesarMensajeSala(msg: com.example.mendacium.ui.model.MensajeJuego) {
        when (msg.tipo) {
            "JUGADOR_UNIDO" -> wsCodigo?.let { actualizarSala(it) }
            "RESULTADO_NOCHE" -> {
                val p = gson.fromJson(msg.payload, PayloadResultadoNoche::class.java)
                _uiState.update { st ->
                    val updated = st.players.map { it.copy(isAlive = p.jugadoresVivos.contains(it.name)) }
                    val eliminado = p.eliminado?.let { name -> updated.find { it.name == name } }
                    st.copy(players = updated, lastEliminated = eliminado, currentPhase = GamePhase.NIGHT_SUMMARY)
                }
            }
            "FASE_DIA" -> {
                val p = gson.fromJson(msg.payload, PayloadFaseDia::class.java)
                _uiState.update { st ->
                    val updated = st.players.map { it.copy(isAlive = p.jugadoresVivos.contains(it.name)) }
                    st.copy(players = updated, numeroDia = p.numeroDia, currentPhase = GamePhase.DAY_DISCUSSION)
                }
            }
            "FASE_VOTACION" -> _uiState.update { it.copy(currentPhase = GamePhase.DAY_VOTING) }
            "RESULTADO_VOTACION" -> {
                val p = gson.fromJson(msg.payload, PayloadResultadoVotacion::class.java)
                _uiState.update { st ->
                    val updated = st.players.map { it.copy(isAlive = p.jugadoresVivos.contains(it.name)) }
                    val linchado = p.linchado?.let { name -> updated.find { it.name == name } }
                    st.copy(players = updated, lastEliminated = linchado, currentPhase = GamePhase.VERDICT)
                }
            }
            "FASE_NOCHE" -> {
                val p = gson.fromJson(msg.payload, PayloadFaseNoche::class.java)
                _uiState.update { st ->
                    val updated = st.players.map { it.copy(isAlive = p.jugadoresVivos.contains(it.name)) }
                    val estoyVivo = p.jugadoresVivos.contains(st.miNombre)
                    val fase = if (!estoyVivo) GamePhase.NIGHT_WAITING else faseNocturnaPara(st.miRol)
                    st.copy(players = updated, numeroDia = p.numeroDia, currentPhase = fase)
                }
            }
            "FIN_PARTIDA" -> {
                val p = gson.fromJson(msg.payload, PayloadFinPartida::class.java)
                val ganador = when (p.ganador) {
                    "BUENOS" -> BandoGanador.BUENOS
                    "IMPOSTORES" -> BandoGanador.IMPOSTORES
                    else -> BandoGanador.NINGUNO
                }
                _uiState.update { st ->
                    val conRoles = st.players.map { pl ->
                        val rev = p.rolesRevelados.find { it.nombre == pl.name }
                        if (rev != null) pl.copy(role = rolDesdeString(rev.rol)) else pl
                    }
                    // Refresca el rol del eliminado con el rol real revelado (no el placeholder)
                    val elimActualizado = st.lastEliminated?.let { le -> conRoles.find { it.name == le.name } }
                    st.copy(
                        players = conRoles,
                        lastEliminated = elimActualizado,
                        winner = ganador,
                        currentPhase = GamePhase.GAME_OVER
                    )
                }
            }
        }
    }

    private fun procesarMensajeJugador(msg: com.example.mendacium.ui.model.MensajeJuego) {
        when (msg.tipo) {
            "PARTIDA_INICIADA" -> {
                val p = gson.fromJson(msg.payload, PayloadPartidaIniciada::class.java)
                val miRol = rolDesdeString(p.rol)
                _uiState.update { st ->
                    st.copy(
                        players = playersDesdeNombres(p.jugadores, st.miNombre, miRol),
                        miRol = miRol,
                        aliados = p.codigosAliados,
                        numeroDia = 1,
                        lastEliminated = null,
                        winner = BandoGanador.NINGUNO,
                        currentPhase = GamePhase.ROLE_REVEAL
                    )
                }
            }
            "RESULTADO_INVESTIGACION" -> {
                val p = gson.fromJson(msg.payload, PayloadResultadoInvestigacion::class.java)
                _uiState.update {
                    it.copy(
                        seerInvestigated = p.investigado,
                        seerRevealEsMalo = p.esMalo,
                        currentPhase = GamePhase.SEER_REVEAL
                    )
                }
            }
            "ACCION_NOCHE_RECIBIDA" -> { /* confirmación; ya pasamos a espera */ }
        }
    }

    // El host inicia la partida en línea (envía la configuración de roles)
    fun hostIniciarPartida(impostorCount: Int, doctorCount: Int, seerCount: Int) {
        val codigo = wsCodigo ?: return
        viewModelScope.launch {
            wsRepository.iniciarPartida(codigo, impostorCount, doctorCount, seerCount)
        }
    }

    fun confirmarRolVisto() {
        _uiState.update { it.copy(currentPhase = faseNocturnaPara(it.miRol)) }
    }

    fun enviarAtaqueImpostor(objetivo: String) = enviarAccionNoche("ATACAR", objetivo)
    fun enviarProteccion(objetivo: String) = enviarAccionNoche("PROTEGER", objetivo)
    fun enviarInvestigacion(objetivo: String) = enviarAccionNoche("INVESTIGAR", objetivo)

    private fun enviarAccionNoche(tipo: String, objetivo: String) {
        val codigo = wsCodigo ?: return
        val nombre = _uiState.value.miNombre
        viewModelScope.launch {
            wsRepository.enviarAccionNoche(codigo, nombre, tipo, objetivo)
            // La vidente espera RESULTADO_INVESTIGACION; los demás pasan a esperar.
            if (tipo != "INVESTIGAR") {
                _uiState.update { it.copy(currentPhase = GamePhase.NIGHT_WAITING) }
            }
        }
    }

    fun enviarVotoOnline(votado: String?) {
        val codigo = wsCodigo ?: return
        val nombre = _uiState.value.miNombre
        viewModelScope.launch {
            wsRepository.enviarVoto(codigo, nombre, votado)
        }
    }

    fun hostContinuarDia() {
        val codigo = wsCodigo ?: return
        viewModelScope.launch { wsRepository.continuarDia(codigo) }
    }

    fun hostIniciarVotacion() {
        val codigo = wsCodigo ?: return
        viewModelScope.launch { wsRepository.iniciarVotacion(codigo) }
    }

    fun hostSiguienteNoche() {
        val codigo = wsCodigo ?: return
        viewModelScope.launch { wsRepository.siguienteNoche(codigo) }
    }

    private fun faseNocturnaPara(rol: Role): GamePhase = when (rol) {
        Role.Impostor -> GamePhase.IMPOSTOR_NIGHT
        Role.Doctor -> GamePhase.DOCTOR_NIGHT
        Role.Vidente -> GamePhase.SEER_NIGHT
        else -> GamePhase.NIGHT_WAITING
    }

    private fun rolDesdeString(rol: String): Role = when (rol) {
        "IMPOSTOR" -> Role.Impostor
        "MEDICO" -> Role.Doctor
        "VIDENTE" -> Role.Vidente
        else -> Role.Aldeano
    }

    private fun playersDesdeNombres(nombres: List<String>, miNombre: String, miRol: Role): List<Player> =
        nombres.mapIndexed { i, nombre ->
            Player(
                name = nombre,
                levelAndStatus = "LISTO",
                isHost = false,
                iconType = IconType.LISTO,
                avatarColorIndex = i % 12,
                role = if (nombre == miNombre) miRol else Role.Aldeano,
                isAlive = true
            )
        }

    // ───────────────────────────── Modo LOCAL (sin cambios) ─────────────────────────────

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
            val linchado = currentState.players.find { it.name == linchadoName }
            val updatedPlayers = MotorResolucion.resolverLinchamiento(currentState.players, linchado)
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
        wsCodigo = null
        viewModelScope.launch {
            try {
                wsRepository.desconectar()
            } catch (_: Exception) {
            }
        }
    }
}
