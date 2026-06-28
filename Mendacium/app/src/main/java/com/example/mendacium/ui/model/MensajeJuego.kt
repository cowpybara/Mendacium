package com.example.mendacium.ui.model

import com.google.gson.JsonElement

// Envoltorio que llega por WebSocket: { "tipo": "...", "payload": { ... } }
// payload se deja como JsonElement y se reinterpreta con Gson según el tipo.
data class MensajeJuego(
    val tipo: String,
    val payload: JsonElement?
)

// ─────────── Payloads tipados (se parsean con Gson desde MensajeJuego.payload) ───────────

data class PayloadPartidaIniciada(
    val rol: String,
    val jugadores: List<String>,
    val codigosAliados: List<String>
)

data class PayloadResultadoNoche(
    val eliminado: String?,
    val nadieMurio: Boolean,
    val jugadoresVivos: List<String>
)

data class PayloadResultadoInvestigacion(
    val investigado: String,
    val esMalo: Boolean
)

data class PayloadFaseDia(
    val numeroDia: Int,
    val jugadoresVivos: List<String>
)

data class PayloadFaseNoche(
    val numeroDia: Int,
    val jugadoresVivos: List<String>
)

data class PayloadFaseVotacion(
    val jugadoresVivos: List<String>
)

data class PayloadResultadoVotacion(
    val linchado: String?,
    val eraImpostor: Boolean?,
    val jugadoresVivos: List<String>
)

data class RolRevelado(
    val nombre: String,
    val rol: String
)

data class PayloadFinPartida(
    val ganador: String,
    val rolesRevelados: List<RolRevelado>
)
