package edu.ucne.composeTarea1.tareas.Presentation.Game.HistorialPartidas

import edu.ucne.composeTarea1.domain.model.Partida

data class PartidaConGanador(
    val partida: Partida,
    val nombreGanador: String? = null
)
