package edu.ucne.composeTarea1.tareas.Presentation.Jugador.list

import edu.ucne.composeTarea1.domain.model.Jugador

data class ListJugadorUiState(
    val jugadores: List<Jugador> = emptyList(),
    val isLoading: Boolean = true,
)
