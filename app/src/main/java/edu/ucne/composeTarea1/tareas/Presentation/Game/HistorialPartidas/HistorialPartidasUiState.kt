package edu.ucne.composeTarea1.tareas.Presentation.Game.HistorialPartidas

data class HistorialPartidasUiState(
    val partidas: List<PartidaConGanador> = emptyList(),
    val isLoading: Boolean = true
)
