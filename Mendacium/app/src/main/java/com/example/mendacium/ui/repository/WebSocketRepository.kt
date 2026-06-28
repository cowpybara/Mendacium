package com.example.mendacium.ui.repository

import com.example.mendacium.ui.model.MensajeJuego
import com.example.mendacium.ui.service.StompClient
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText

// Capa que traduce entre el juego y el WebSocket: suscribe a destinos y envía mensajes.
class WebSocketRepository {

    private val gson = Gson()
    private var session: StompSession? = null

    suspend fun conectar() {
        session = StompClient.conectar()
    }

    suspend fun desconectar() {
        StompClient.desconectar()
        session = null
    }

    // Mensajes de broadcast de la sala (lobby, fases, resultados)
    suspend fun suscribirSala(codigo: String): Flow<MensajeJuego> =
        suscribir("/topic/sala/$codigo")

    // Mensajes privados de este jugador (su rol, confirmaciones, investigación)
    suspend fun suscribirJugador(nombre: String): Flow<MensajeJuego> =
        suscribir("/queue/jugador/$nombre")

    // subscribeText es suspend en Krossbow (envía el frame SUBSCRIBE), por eso esta función lo es.
    private suspend fun suscribir(destino: String): Flow<MensajeJuego> {
        val s = session ?: return emptyFlow()
        return s.subscribeText(destino).map { texto ->
            gson.fromJson(texto, MensajeJuego::class.java)
        }
    }

    // ─────────── Envíos al servidor ───────────

    suspend fun iniciarPartida(codigo: String, impostorCount: Int, doctorCount: Int, seerCount: Int) {
        enviar("/app/sala/$codigo/iniciar", mapOf(
            "impostorCount" to impostorCount,
            "doctorCount" to doctorCount,
            "seerCount" to seerCount
        ))
    }

    suspend fun enviarAccionNoche(codigo: String, jugador: String, tipo: String, objetivo: String?) {
        enviar("/app/sala/$codigo/accion", mapOf(
            "jugador" to jugador,
            "tipo" to tipo,
            "objetivo" to objetivo
        ))
    }

    suspend fun enviarVoto(codigo: String, jugador: String, votado: String?) {
        enviar("/app/sala/$codigo/votar", mapOf(
            "jugador" to jugador,
            "votado" to votado
        ))
    }

    suspend fun continuarDia(codigo: String) = enviar("/app/sala/$codigo/continuarDia", emptyMap())
    suspend fun iniciarVotacion(codigo: String) = enviar("/app/sala/$codigo/iniciarVotacion", emptyMap())
    suspend fun siguienteNoche(codigo: String) = enviar("/app/sala/$codigo/siguienteNoche", emptyMap())

    private suspend fun enviar(destino: String, body: Map<String, Any?>) {
        session?.sendText(destino, gson.toJson(body))
    }
}
