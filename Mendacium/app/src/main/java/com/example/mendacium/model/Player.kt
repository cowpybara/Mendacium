package com.example.mendacium.model

import androidx.compose.ui.graphics.Color
import com.example.mendacium.ui.theme.ImpostorRed

enum class IconType {
    LISTO,
    CONECTANDO,
    ESTRELLA, //Amigo Destacado
    NINGUNO
}

sealed class Role(
    val name: String,
    val description: String,
    val color: Color
) {
    object Vidente : Role(
        "VIDENTE",
        "Tú ves lo que otros ocultan. Investiga la identidad de un jugador cada noche.",
        Color(0xFFD8B4FE)
    )
    object Impostor : Role(
        "IMPOSTOR",
        "Siembra el caos. Elimina a los demás jugadores sin ser descubierto.",
        ImpostorRed
    )
    object Doctor : Role(
        "MÉDICO",
        "Protege la vida. Elige a alguien cada noche para salvarlo de un ataque.",
        Color(0xFF4ADE80)
    )
    object Aldeano : Role(
        "ALDEANO",
        "Busca la verdad. Encuentra a los impostores antes de que sea tarde.",
        Color(0xFF60A5FA)
    )
}

data class Player (
    val name: String,
    val levelAndStatus: String,
    val isHost: Boolean = false,
    val iconType: IconType,
    val role: Role = Role.Aldeano,
    val isAlive: Boolean = true
)