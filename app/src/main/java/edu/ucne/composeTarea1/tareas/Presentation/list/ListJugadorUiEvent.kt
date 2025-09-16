package edu.ucne.composeTarea1.tareas.Presentation.list

import edu.ucne.composeTarea1.domain.model.Jugador

interface ListJugadorUiEvent {
    data class OnDeleteJugadorClick(val jugador: Jugador) : ListJugadorUiEvent
}