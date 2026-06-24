package com.example.mendacium.ui.service

import com.example.mendacium.ui.repository.SalaRepository

object ServiceLocator {
    private val salaApi = RetrofitClient.instance
    val salaRepository = SalaRepository(salaApi)
}
