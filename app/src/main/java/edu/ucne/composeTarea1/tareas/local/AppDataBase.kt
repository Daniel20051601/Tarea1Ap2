package edu.ucne.composeTarea1.tareas.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ucne.composeTarea1.tareas.local.Jugador.JugadorDao
import edu.ucne.composeTarea1.tareas.local.Jugador.JugadorEntity
import edu.ucne.composeTarea1.tareas.local.Partida.PartidaDao
import edu.ucne.composeTarea1.tareas.local.Partida.PartidaEntity

@Database(
    entities = [
        JugadorEntity::class,
        PartidaEntity::class
    ],
    version = 7,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao
    abstract fun PartidaDao(): PartidaDao
}