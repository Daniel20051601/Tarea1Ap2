package edu.ucne.composeTarea1.domain.repository

import edu.ucne.composeTarea1.domain.model.Partida
import kotlinx.coroutines.flow.Flow

interface PartidaRepository {
    fun observeAll(): Flow<List<Partida>>
    suspend fun getById(id: Int): Partida?
    suspend fun upsert(partida: Partida)
    suspend fun delete(partida: Partida)
    fun getPartidaEnProgreso(): Flow<Partida?>
    suspend fun clearPartidasEnProgreso()
}