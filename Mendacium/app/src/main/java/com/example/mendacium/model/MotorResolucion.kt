package com.example.mendacium.model

data class ResultadoNoche(
    val jugadorEliminado: Player?,
    val jugadoresActualizados: List<Player>
)

enum class BandoGanador {
    BUENOS, IMPOSTORES, NINGUNO
}

object MotorResolucion {

    fun resolverNoche(
        jugadores: List<Player>,
        victimaImpostor: String?,
        protegidoMedico: String?
    ): ResultadoNoche {

        // Si hay víctima y no coincide con el protegido por el médico, entonces es eliminado.
        val nombreEliminado = when {
            victimaImpostor == null -> null
            victimaImpostor == protegidoMedico -> null // ¡El médico llegó a tiempo!
            else -> victimaImpostor
        }

        // Actualizamos la lista clonando a los jugadores, pero marcando isAlive = false al que le tocó perder
        val jugadoresActualizados = jugadores.map { jugador ->
            if (jugador.name == nombreEliminado) {
                jugador.copy(isAlive = false)
            } else {
                jugador
            }
        }

        // Buscamos el objeto Player del eliminado para retornarlo
        val jugadorEliminado = jugadoresActualizados.find { it.name == nombreEliminado }

        return ResultadoNoche(jugadorEliminado, jugadoresActualizados)
    }

    //Actualiza la lista de jugadores tras la votación de día.
    fun resolverLinchamiento(
        jugadores: List<Player>,
        linchado: Player?
    ): List<Player> {
        // Si el linchado es null, significa que se abstuvieron. Nadie muere.
        if (linchado == null) return jugadores

        return jugadores.map { jugador ->
            if (jugador.name == linchado.name) {
                jugador.copy(isAlive = false)
            } else {
                jugador
            }
        }
    }


    fun evaluarVictoria(jugadores: List<Player>): BandoGanador {
        val vivos = jugadores.filter { it.isAlive }
        val impostoresVivos = vivos.count { it.role == Role
            .Impostor }
        val buenosVivos = vivos.size - impostoresVivos

        return when {
            impostoresVivos == 0 -> BandoGanador.BUENOS
            impostoresVivos >= buenosVivos -> BandoGanador.IMPOSTORES
            else -> BandoGanador.NINGUNO
        }
    }
}