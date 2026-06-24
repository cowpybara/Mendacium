package com.example.mendacium.ui.repository

import com.example.mendacium.ui.model.Sala
import com.example.mendacium.ui.service.ApiResult
import com.example.mendacium.ui.service.SalaService

class SalaRepository(private val api: SalaService) {

    suspend fun crearSala(hostNombre: String): ApiResult<Sala> {
        return try {
            val response = api.crearSala(mapOf("hostNombre" to hostNombre))
            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!)
            } else {
                ApiResult.Error("Error HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun unirseASala(codigo: String, nombre: String): ApiResult<Sala> {
        return try {
            val joinResponse = api.unirse(codigo, mapOf("nombre" to nombre))
            if (!joinResponse.isSuccessful) {
                return ApiResult.Error("Código inválido o sala no disponible")
            }
            val salaResponse = api.obtenerSala(codigo)
            if (salaResponse.isSuccessful) {
                ApiResult.Success(salaResponse.body()!!)
            } else {
                ApiResult.Error("Error al obtener sala: ${salaResponse.code()}")
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun obtenerSala(codigo: String): ApiResult<Sala> {
        return try {
            val response = api.obtenerSala(codigo)
            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!)
            } else {
                ApiResult.Error("Error HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error desconocido")
        }
    }
}
