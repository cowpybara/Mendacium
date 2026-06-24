package com.example.mendacium.ui.repository

import android.util.Log
import com.example.mendacium.ui.model.Sala
import com.example.mendacium.ui.service.ApiResult
import com.example.mendacium.ui.service.SalaService

class SalaRepository(private val api: SalaService) {

    suspend fun crearSala(hostNombre: String): ApiResult<Sala> {
        return try {
            Log.d("SalaRepository", "crearSala → hostNombre=$hostNombre")
            val response = api.crearSala(mapOf("hostNombre" to hostNombre))
            if (response.isSuccessful) {
                Log.d("SalaRepository", "crearSala ✓ codigo=${response.body()?.codigo}")
                ApiResult.Success(response.body()!!)
            } else {
                Log.e("SalaRepository", "crearSala ✗ HTTP ${response.code()}")
                ApiResult.Error("Error HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("SalaRepository", "crearSala ✗ excepción: ${e.message}")
            ApiResult.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun unirseASala(codigo: String, nombre: String): ApiResult<Sala> {
        return try {
            Log.d("SalaRepository", "unirse → codigo=$codigo nombre=$nombre")
            val joinResponse = api.unirse(codigo, mapOf("nombre" to nombre))
            if (!joinResponse.isSuccessful) {
                Log.e("SalaRepository", "unirse ✗ HTTP ${joinResponse.code()}")
                return ApiResult.Error("Código inválido o sala no disponible")
            }
            val salaResponse = api.obtenerSala(codigo)
            if (salaResponse.isSuccessful) {
                Log.d("SalaRepository", "unirse ✓ sala obtenida con ${salaResponse.body()?.jugadores?.size} jugadores")
                ApiResult.Success(salaResponse.body()!!)
            } else {
                Log.e("SalaRepository", "obtenerSala ✗ HTTP ${salaResponse.code()}")
                ApiResult.Error("Error al obtener sala: ${salaResponse.code()}")
            }
        } catch (e: Exception) {
            Log.e("SalaRepository", "unirse ✗ excepción: ${e.message}")
            ApiResult.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun obtenerSala(codigo: String): ApiResult<Sala> {
        return try {
            val response = api.obtenerSala(codigo)
            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!)
            } else {
                Log.e("SalaRepository", "obtenerSala ✗ HTTP ${response.code()}")
                ApiResult.Error("Error HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("SalaRepository", "obtenerSala ✗ excepción: ${e.message}")
            ApiResult.Error(e.message ?: "Error desconocido")
        }
    }
}
