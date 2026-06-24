package com.example.mendacium.model

import androidx.compose.ui.graphics.Color
import com.example.mendacium.ui.theme.ImpostorRed

// Esta clase extra nos sirve para devolver de forma limpia qué pasó en la noche
data class ResultadoAccion(val mensaje: String, val objetivo: Player?)

sealed class Role(
    val name: String,
    val description: String,
    val color: Color,
    val faction: String
) {
    // Aquí está el polimorfismo: cada rol DEBE decir cómo actúa de noche.
    abstract fun realizarAccion(objetivo: Player? = null): ResultadoAccion

    object Vidente : Role(
        "VIDENTE",
        "Tú ves lo que otros ocultan. Investiga la identidad de un jugador cada noche para encontrar la verdad.",
        Color(0xFFD8B4FE),
        "BANDO BUENO"
    ) {
        override fun realizarAccion(objetivo: Player?): ResultadoAccion {
            return if (objetivo != null) {
                ResultadoAccion("Has investigado a ${objetivo.name}. Es del ${objetivo.role.faction}.", objetivo)
            } else {
                ResultadoAccion("No lograste investigar a nadie.", null)
            }
        }
    }

    object Impostor : Role(
        "IMPOSTOR",
        "La mentira es tu única arma. Despierta cada noche para eliminar a un jugador en silencio.",
        ImpostorRed,
        "BANDO IMPOSTOR"
    ) {
        override fun realizarAccion(objetivo: Player?): ResultadoAccion {
            return if (objetivo != null) {
                ResultadoAccion("Has atacado a ${objetivo.name} en las sombras.", objetivo)
            } else {
                ResultadoAccion("Te quedaste quieto esta noche.", null)
            }
        }
    }

    object Doctor : Role(
        "MÉDICO",
        "La vida que salvas decide la partida. Cada noche, protege a un jugador del ataque del impostor.",
        Color(0xFF4ADE80),
        "BANDO BUENO"
    ) {
        override fun realizarAccion(objetivo: Player?): ResultadoAccion {
            return if (objetivo != null) {
                ResultadoAccion("Has protegido a ${objetivo.name} con éxito.", objetivo)
            } else {
                ResultadoAccion("No protegiste a nadie esta noche.", null)
            }
        }
    }

    object Aldeano : Role(
        "ALDEANO",
        "Tu voz es tu única defensa. No tienes habilidades nocturnas. Deduce y vota de día.",
        Color(0xFF60A5FA),
        "BANDO BUENO"
    ) {
        override fun realizarAccion(objetivo: Player?): ResultadoAccion {
            // El aldeano no hace nada de noche, su objetivo siempre es nulo
            return ResultadoAccion("Dormiste toda la noche sin enterarte de nada.", null)
        }
    }
}