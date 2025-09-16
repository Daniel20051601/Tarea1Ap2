package edu.ucne.composeTarea1.domain.model

data class Partida(
    val partidaId: Int = 0,
    val fecha: String,
    val jugador1Id: Int?,
    val jugador2Id: Int?,
    val ganadorId: Int?,
    val esFinalizada: Boolean = false
)
