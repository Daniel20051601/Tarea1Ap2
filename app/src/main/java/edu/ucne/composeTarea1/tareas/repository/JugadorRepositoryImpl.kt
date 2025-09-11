package edu.ucne.composeTarea1.tareas.repository

import androidx.compose.runtime.snapshots.toInt
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.tareas.local.JugadorDao
import edu.ucne.composeTarea1.tareas.mapper.toDomain
import edu.ucne.composeTarea1.tareas.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JugadorRepositoryImpl @Inject constructor(private val dao: JugadorDao): JugadorRepository {

    override fun observeJugador(): Flow<List<Jugador>> = dao.observeAll().map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun getJugador(id: Int): Jugador? = dao.getById(id)?.toDomain()

    override suspend fun upsertJugador(jugador: Jugador): Int {
        val generatedId = dao.upsert(jugador.toEntity())
        return generatedId.toInt()
    }

    override suspend fun deleteJugador(id: Int) = dao.delete(id)

    override suspend fun getJugadorByName(name: String): Jugador? {
        return dao.getJugadorByName(name)
    }

}
