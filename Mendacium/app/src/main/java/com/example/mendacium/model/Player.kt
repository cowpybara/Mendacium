package com.example.mendacium.model

enum class IconType {
    LISTO,
    CONECTANDO,
    ESTRELLA, //Amigo Destacado
    NINGUNO
}

data class Player (
    val name: String,
    val levelAndStatus: String,
    val isHost: Boolean = false,
    val iconType: IconType
)