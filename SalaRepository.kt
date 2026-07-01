package com.example.mendacium.ui.model

data class Sala(
    val id: String,
    val codigo: String,
    val estadoSala: String,
    val jugadores: List<Jugador>
)
