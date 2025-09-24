package edu.ucne.composeTarea1.tareas.local.Logro

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LogroDao {
    @Query("SELECT * FROM Logros ORDER BY logroId DESC")
    fun observeAll(): Flow<List<LogroEntity>>

    @Query("SELECT * FROM Logros WHERE logroId = :id")
    suspend fun getById(id: Int): LogroEntity?

    @Upsert
    suspend fun upsert(logro: LogroEntity): Long

    @Delete
    suspend fun delete(logro: LogroEntity)

    @Query("DELETE FROM Logros WHERE logroId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM Logros WHERE nombre COLLATE NOCASE = :nombre COLLATE NOCASE LIMIT 1")
    suspend fun getByNombre(nombre: String): LogroEntity?

}