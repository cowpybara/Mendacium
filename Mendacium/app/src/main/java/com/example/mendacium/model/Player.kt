package com.example.mendacium.model

enum class IconType {
    LISTO,
    CONECTANDO,
    ESTRELLA,
    NINGUNO
}

data class Player(
    val name: String,
    val levelAndStatus: String,
    val isHost: Boolean = false,
    val iconType: IconType,
    val avatarColorIndex: Int = 0,
    val role: Role = Role.Aldeano,
    val isAlive: Boolean = true
)