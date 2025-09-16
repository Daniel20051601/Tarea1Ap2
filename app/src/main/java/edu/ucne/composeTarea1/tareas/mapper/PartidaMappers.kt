package edu.ucne.composeTarea1.tareas.mapper

import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.tareas.local.Partida.PartidaEntity

fun PartidaEntity.toDomain(): Partida = Partida(
    partidaId = partidaId,
    fecha = fecha,
    jugador1Id = jugador1Id,
    jugador2Id = jugador2Id,
    ganadorId = ganadorId,
    esFinalizada = esFinalizada
)

fun Partida.toEntity(): PartidaEntity = PartidaEntity(
    partidaId = partidaId,
    fecha = fecha,
    jugador1Id = jugador1Id,
    jugador2Id = jugador2Id,
    ganadorId = ganadorId,
    esFinalizada = esFinalizada
)