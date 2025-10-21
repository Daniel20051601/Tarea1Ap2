package edu.ucne.composeTarea1.tareas.local.Logro

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Logros")
data class LogroEntity(
    @PrimaryKey(autoGenerate = true)
    val logroId: Int = 0,
    val nombre: String,
    val descripcion: String,
    val isCompletado: Boolean,
)
