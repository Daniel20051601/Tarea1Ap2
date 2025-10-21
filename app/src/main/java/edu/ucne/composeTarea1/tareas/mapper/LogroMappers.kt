package edu.ucne.composeTarea1.tareas.mapper

import edu.ucne.composeTarea1.domain.model.Logro
import edu.ucne.composeTarea1.tareas.local.Logro.LogroEntity

fun LogroEntity.toDomain(): Logro = Logro(
    logroId = logroId,
    nombre = nombre,
    descripcion = descripcion,
    isCompletado = isCompletado
)
fun Logro.toEntity(): LogroEntity = LogroEntity(
    logroId = logroId,
    nombre = nombre,
    descripcion = descripcion,
    isCompletado = isCompletado
)