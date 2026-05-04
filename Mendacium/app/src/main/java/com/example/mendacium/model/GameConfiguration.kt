package com.example.mendacium.model

data class GameConfiguration(
    val totalPlayers: Int = DEFAULT_TOTAL_PLAYERS,
    val impostorCount: Int = DEFAULT_IMPOSTOR_COUNT,
    val doctorCount: Int = 1,
    val seerCount: Int = 1
) {
    val villagerCount: Int
        get() = totalPlayers - impostorCount - doctorCount - seerCount

    fun hasAtLeastOneVillager(): Boolean {
        return villagerCount >= MIN_VILLAGER_COUNT
    }

    companion object {
        const val MIN_TOTAL_PLAYERS = 4
        const val MAX_TOTAL_PLAYERS = 12
        const val MIN_IMPOSTOR_COUNT = 1
        const val MIN_VILLAGER_COUNT = 1
        const val DEFAULT_TOTAL_PLAYERS = 12
        const val DEFAULT_IMPOSTOR_COUNT = 2
    }
}
