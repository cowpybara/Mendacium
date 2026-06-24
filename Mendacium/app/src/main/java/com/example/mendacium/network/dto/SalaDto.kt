package com.example.mendacium.network.dto

data class JugadorDto(
    val id: String,
    val nombre: String,
    val esHost: Boolean,
    val rol: String?,
    val vivo: Boolean
)

data class SalaDto(
    val id: String,
    val codigo: String,
    val estadoSala: String,
    val jugadores: List<JugadorDto>
)
