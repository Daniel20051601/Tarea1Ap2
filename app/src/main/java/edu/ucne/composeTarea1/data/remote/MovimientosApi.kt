package edu.ucne.composeTarea1.data.remote

import edu.ucne.composeTarea1.data.remote.dto.MovimientosDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface MovimientosApi {
    @GET("api/Movimientos/{id}")
    suspend fun getMovimientos(@Path("id") id: Int): List<MovimientosDto>

    @POST("api/Movimientos")
    suspend fun postMovimiento(@Body movimiento: MovimientosDto): Response<Unit>


}