package edu.ucne.composeTarea1.tareas.local.Partida

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import edu.ucne.composeTarea1.tareas.local.Jugador.JugadorEntity

@Entity(
    tableName = "Partidas",
    foreignKeys = [
        ForeignKey(
            entity = JugadorEntity::class,
            parentColumns = ["jugadorId"],
            childColumns = ["jugador1Id"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = JugadorEntity::class,
            parentColumns = ["jugadorId"],
            childColumns = ["jugador2Id"],
            onDelete = ForeignKey.SET_NULL
        ),
    ForeignKey(
        entity = JugadorEntity::class,
        parentColumns = ["jugadorId"],
        childColumns = ["ganadorId"],
        onDelete = ForeignKey.SET_NULL
    )
    ]
)

data class PartidaEntity(
    @PrimaryKey(autoGenerate = true)
    var partidaId: Int = 0,
    val fecha: String,
    val jugador1Id: Int?,
    val jugador2Id: Int?,
    val ganadorId: Int?,
    val esFinalizada: Boolean = false
)
