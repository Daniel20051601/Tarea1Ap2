package edu.ucne.composeTarea1.tareas.repository

import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import edu.ucne.composeTarea1.tareas.local.Partida.PartidaDao
import edu.ucne.composeTarea1.tareas.mapper.toDomain
import edu.ucne.composeTarea1.tareas.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartidaRepositoryImpl @Inject constructor(
    private val partidaDao: PartidaDao
) : PartidaRepository {

    override fun observeAll(): Flow<List<Partida>> {
        return partidaDao.observeAll().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getById(id: Int): Partida? {
        return partidaDao.getById(id)?.toDomain()
    }

    override suspend fun upsert(partida: Partida) {
        partidaDao.upsert(partida.toEntity())
    }

    override suspend fun delete(partida: Partida) {
        partidaDao.delete(partida.toEntity())
    }
}