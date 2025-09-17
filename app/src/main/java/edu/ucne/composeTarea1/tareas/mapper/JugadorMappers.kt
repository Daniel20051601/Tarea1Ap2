package edu.ucne.composeTarea1.tareas.mapper

import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.tareas.local.Jugador.JugadorEntity

fun JugadorEntity.toDomain(): Jugador = Jugador(
    jugadorId = jugadorId,
    nombres = nombres,
    partidas = partidas,

)
fun Jugador.toEntity(): JugadorEntity = JugadorEntity(
    jugadorId = jugadorId,
    nombres = nombres,
    partidas = partidas,
)