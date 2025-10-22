package edu.ucne.composeTarea1.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovimientosDto(
    val movimientoId: Int = 0,
    val partidaId: Int = 0,
    val jugador: String,
    val posicionFila: Int,
    val posicionColumna: Int,
)
