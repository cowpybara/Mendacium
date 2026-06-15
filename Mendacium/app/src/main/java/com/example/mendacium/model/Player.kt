package com.example.mendacium.model

import androidx.compose.ui.graphics.Color
import com.example.mendacium.ui.theme.ImpostorRed

enum class IconType {
    LISTO,
    CONECTANDO,
    ESTRELLA,
    NINGUNO
}

sealed class Role(
    val name: String,
    val description: String,
    val color: Color,
    val faction: String
) {
    object Vidente : Role(
        "VIDENTE",
        "Tú ves lo que otros ocultan. Investiga la identidad de un jugador cada noche para encontrar la verdad.",
        Color(0xFFD8B4FE),
        "BANDO BUENO"
    )
    object Impostor : Role(
        "IMPOSTOR",
        "La mentira es tu única arma. Despierta cada noche para eliminar a un jugador en silencio.",
        ImpostorRed,
        "BANDO IMPOSTOR"
    )
    object Doctor : Role(
        "MÉDICO",
        "La vida que salvas decide la partida. Cada noche, protege a un jugador del ataque del impostor.",
        Color(0xFF4ADE80),
        "BANDO BUENO"
    )
    object Aldeano : Role(
        "ALDEANO",
        "Tu voz es tu única defensa. No tienes habilidades nocturnas. Deduce y vota de día.",
        Color(0xFF60A5FA),
        "BANDO BUENO"
    )
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
