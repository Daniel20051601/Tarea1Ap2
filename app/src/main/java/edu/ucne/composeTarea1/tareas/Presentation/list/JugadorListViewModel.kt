package edu.ucne.composeTarea1.tareas.Presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JugadorListViewModel @Inject constructor(
    private val repository: JugadorRepository
) : ViewModel() {
    val state: StateFlow<ListJugadorUiState> =
        repository.observeJugador()
            .map { jugadores ->
                val victorias = jugadores.find { it.nombres == "Yo" }?.partidas ?: 0

                val otrosJugadores = jugadores.filter { it.nombres != "Yo" }

                ListJugadorUiState(
                    jugadores = otrosJugadores,
                    misVictorias = victorias,
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = ListJugadorUiState(isLoading = true)
            )
    fun onEvent(event: ListJugadorUiEvent) {
        when (event) {
            is ListJugadorUiEvent.OnDeleteJugadorClick -> {
                viewModelScope.launch {
                    repository.deleteJugador(event.jugador.jugadorId)
                }
            }
        }
    }
}