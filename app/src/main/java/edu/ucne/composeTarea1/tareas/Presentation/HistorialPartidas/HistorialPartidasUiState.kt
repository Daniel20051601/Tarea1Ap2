package edu.ucne.composeTarea1.tareas.Presentation.HistorialPartidas

import edu.ucne.composeTarea1.domain.model.Partida

data class HistorialPartidasUiState(
    val partidas: List<PartidaConGanador> = emptyList(),
    val isLoading: Boolean = true
)
