package edu.ucne.composeTarea1.tareas.mapper

import edu.ucne.composeTarea1.data.remote.dto.MovimientosDto
import edu.ucne.composeTarea1.domain.model.Movimientos

fun MovimientosDto.toMovimientos(): Movimientos = Movimientos(
    movimientoId = movimientoId,
    partidaId = partidaId,
    jugador = jugador,
    posicionFila = posicionFila,
    posicionColumna = posicionColumna,
)

fun Movimientos.toDto(partidaId: Int): MovimientosDto = MovimientosDto(
    movimientoId = movimientoId,
    partidaId = partidaId,
    jugador = jugador,
    posicionFila = posicionFila,
    posicionColumna = posicionColumna,
)