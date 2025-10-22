package edu.ucne.composeTarea1.tareas.local.Partida

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {
    @Upsert
    suspend fun upsert(partida: PartidaEntity)

    @Delete
    suspend fun delete(partida: PartidaEntity)

    @Query("SELECT * FROM Partidas WHERE partidaId = :id ")
    suspend fun getById(id: Int): PartidaEntity?

    @Query("SELECT * FROM Partidas ORDER BY fecha DESC")
    fun observeAll(): Flow<List<PartidaEntity>>

    @Query("SELECT * FROM Partidas WHERE jugador1Id = :jugadorId OR jugador2Id = :jugadorId ")
    fun observePartidasDeJugador(jugadorId: Int): Flow<List<PartidaEntity>>

    @Query("SELECT * FROM Partidas WHERE esFinalizada = 0 LIMIT 1")
    fun getPartidaEnProgreso(): Flow<PartidaEntity?>

    @Query("DELETE FROM Partidas WHERE esFinalizada = 0")
    suspend fun clearPartidasEnProgreso()

}