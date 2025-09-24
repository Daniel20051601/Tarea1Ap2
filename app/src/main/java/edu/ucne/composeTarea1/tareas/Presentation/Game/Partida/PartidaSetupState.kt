package edu.ucne.composeTarea1.tareas.Presentation.Game.Partida

import edu.ucne.composeTarea1.domain.model.Jugador

data class PartidaSetupState(
    val jugadorIzquierda: Jugador? = null,
    val jugadorDerecha: Jugador? = null,
    val listaOponentes: List<Jugador> = emptyList(),
    val isLoading: Boolean = true
)
