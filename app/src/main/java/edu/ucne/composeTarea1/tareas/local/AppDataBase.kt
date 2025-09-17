package edu.ucne.composeTarea1.tareas.local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.composeTarea1.tareas.local.Jugador.JugadorDao
import edu.ucne.composeTarea1.tareas.local.Jugador.JugadorEntity
import edu.ucne.composeTarea1.tareas.local.Partida.PartidaDao
import edu.ucne.composeTarea1.tareas.local.Partida.PartidaEntity

@Database(
    entities = [
        JugadorEntity::class,
        PartidaEntity::class
    ],
    version = 5,
    exportSchema = false,
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao
    abstract fun PartidaDao(): PartidaDao
}