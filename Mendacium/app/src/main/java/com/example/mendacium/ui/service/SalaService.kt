package com.example.mendacium.ui.service

import com.example.mendacium.ui.model.Jugador
import com.example.mendacium.ui.model.Sala
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SalaService {

    @POST("sala/crear")
    suspend fun crearSala(@Body body: Map<String, String>): Response<Sala>

    @POST("sala/{codigo}/unirse")
    suspend fun unirse(
        @Path("codigo") codigo: String,
        @Body body: Map<String, String>
    ): Response<Jugador>

    @GET("sala/getCodigo/{codigo}")
    suspend fun obtenerSala(@Path("codigo") codigo: String): Response<Sala>
}
