package edu.ucne.composeTarea1.tareas.mapper

import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.tareas.Presentation.Game.Partida.Player
import edu.ucne.composeTarea1.tareas.local.Converters
import edu.ucne.composeTarea1.tareas.local.Partida.PartidaEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun PartidaEntity.toDomain(): Partida {
    val boardList = Converters().fromStringToBoard(this.boardState)
    return Partida(
        partidaId = this.partidaId,
        fecha = this.fecha,
        jugador1Id = this.jugador1Id,
        jugador2Id = this.jugador2Id,
        ganadorId = this.ganadorId,
        esFinalizada = this.esFinalizada,
        boardState = boardList,
        currentPlayer = this.currentPlayer
    )
}

fun Partida.toEntity(): PartidaEntity {
    val boardString = Converters().fromBoardToString(this.boardState)
    return PartidaEntity(
        partidaId = this.partidaId,
        fecha = this.fecha,
        jugador1Id = this.jugador1Id,
        jugador2Id = this.jugador2Id,
        ganadorId = this.ganadorId,
        esFinalizada = this.esFinalizada,
        boardState = boardString,
        currentPlayer = this.currentPlayer
    )
}
