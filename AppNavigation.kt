package com.example.mendacium.ui.model

data class Jugador(
    val id: String,
    var nombre: String,
    val esHost: Boolean,
    val rol: String?,
    val vivo: Boolean
)