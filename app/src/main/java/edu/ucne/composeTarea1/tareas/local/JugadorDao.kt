package edu.ucne.composeTarea1.tareas.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.composeTarea1.domain.model.Jugador
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadorDao {
    @Query("SELECT * FROM Jugadores ORDER BY jugadorId DESC")
    fun observeAll(): Flow<List<JugadorEntity>>

    @Query("SELECT * FROM Jugadores WHERE jugadorId = :id")
    suspend fun getById(id: Int): JugadorEntity?

    @Upsert
    suspend fun upsert(jugador: JugadorEntity)

    @Delete
    suspend fun delete(jugador: JugadorEntity)

    @Query("DELETE FROM Jugadores WHERE jugadorId = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM jugadores WHERE Nombres COLLATE NOCASE = :name COLLATE NOCASE LIMIT 1")
    suspend fun getJugadorByName(name: String): Jugador?


}