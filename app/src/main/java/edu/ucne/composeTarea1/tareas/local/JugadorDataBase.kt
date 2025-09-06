package edu.ucne.composeTarea1.tareas.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        JugadorEntity::class
    ],
    version = 2,
    exportSchema = false,
)
abstract class JugadorDataBase : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao
}