package com.example.mendacium.ui.service

import com.example.mendacium.ui.repository.SalaRepository
import com.example.mendacium.ui.repository.WebSocketRepository

object ServiceLocator {
    private val salaApi = RetrofitClient.instance
    val salaRepository = SalaRepository(salaApi)
    val wsRepository = WebSocketRepository()
}
