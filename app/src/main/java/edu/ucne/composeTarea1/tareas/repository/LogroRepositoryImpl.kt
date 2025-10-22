package edu.ucne.composeTarea1.tareas.repository

import edu.ucne.composeTarea1.domain.model.Logro
import edu.ucne.composeTarea1.domain.repository.LogroRepository
import edu.ucne.composeTarea1.tareas.local.Logro.LogroDao
import edu.ucne.composeTarea1.tareas.mapper.toDomain
import edu.ucne.composeTarea1.tareas.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogroRepositoryImpl @Inject constructor(private val dao: LogroDao): LogroRepository {
    override suspend fun save(logro: Logro){
        dao.upsert(logro.toEntity())
    }

    override fun observeLogro(): Flow<List<Logro>> = dao.observeAll().map { list -> list.map {it.toDomain() } }

    override suspend fun getById(id: Int): Logro? = dao.getById(id)?.toDomain()

    override suspend fun upsertLogro(logro: Logro): Int {
        val generateId = dao.upsert(logro.toEntity())
        return generateId.toInt()
    }

    override suspend fun deleteById(id: Int) = dao.deleteById(id)

    override suspend fun getByNombre(nombre: String): Logro? {
        return dao.getByNombre(nombre)?.toDomain()
    }
}