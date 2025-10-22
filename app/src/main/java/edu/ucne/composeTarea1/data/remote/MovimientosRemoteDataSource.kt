package edu.ucne.composeTarea1.data.remote

import edu.ucne.composeTarea1.data.remote.dto.MovimientosDto
import javax.inject.Inject

class MovimientosRemoteDataSource @Inject constructor(
    private val api: MovimientosApi
) {
    suspend fun getMovimientos(partidaId: Int) = api.getMovimientos(partidaId)
    suspend fun postMovimiento(partidaId: Int, movimiento: MovimientosDto) =
        api.postMovimiento(movimiento)
}