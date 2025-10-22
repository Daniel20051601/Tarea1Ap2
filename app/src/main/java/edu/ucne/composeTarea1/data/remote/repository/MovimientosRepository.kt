package edu.ucne.composeTarea1.data.remote.repository

import edu.ucne.composeTarea1.data.remote.MovimientosRemoteDataSource
import edu.ucne.composeTarea1.domain.model.Movimientos
import edu.ucne.composeTarea1.tareas.mapper.toDto
import edu.ucne.composeTarea1.tareas.mapper.toMovimientos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovimientosRepository @Inject constructor(
    private val remoteDataSource: MovimientosRemoteDataSource
) {
    suspend fun getMovimientos(partidaId: Int): Flow<List<Movimientos>> = flow {
        val response = remoteDataSource.getMovimientos(partidaId)
        val movimientos = response.map {
            it.toMovimientos().copy(partidaId = partidaId)
        }
        emit(movimientos)
    }.catch { e ->
        e.printStackTrace()
        emit(emptyList())
    }

    suspend fun postMovimiento(partidaId: Int, movimiento: Movimientos): Boolean {
        try {
            val response = remoteDataSource.postMovimiento(partidaId, movimiento.toDto(partidaId))
            return response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}