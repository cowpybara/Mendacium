package com.example.mendacium.network

import com.example.mendacium.network.dto.SalaDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MendaciumApi {

    @POST("sala/crear")
    suspend fun crearSala(@Body body: Map<String, String>): SalaDto

    @POST("sala/{codigo}/unirse")
    suspend fun unirse(
        @Path("codigo") codigo: String,
        @Body body: Map<String, String>
    ): JugadorResponse

    @GET("sala/getCodigo/{codigo}")
    suspend fun obtenerSala(@Path("codigo") codigo: String): SalaDto
}

data class JugadorResponse(
    val id: String,
    val nombre: String,
    val esHost: Boolean,
    val rol: String?,
    val vivo: Boolean
)
