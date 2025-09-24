package edu.ucne.composeTarea1.domain.repository

import edu.ucne.composeTarea1.domain.model.Logro
import kotlinx.coroutines.flow.Flow

interface LogroRepository {
    suspend fun save(logro: Logro)
    fun observeLogro(): Flow<List<Logro>>
    suspend fun getById(id: Int): Logro?
    suspend fun upsertLogro(logro: Logro): Int
    suspend fun deleteById(id: Int)
    suspend fun getByNombre(nombre: String): Logro?
}