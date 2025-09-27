package edu.ucne.composeTarea1.tareas.Presentation.Jugador.list

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
                ListJugadorUiState(
                    jugadores = jugadores,
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L), // 5 segundos de tiempo de espera antes de que el Flow se detenga si no hay subscriptores
                initialValue = ListJugadorUiState(isLoading = true)
            )

    fun onEvent(event: ListJugadorUiEvent) {
        when (event) {
            is ListJugadorUiEvent.OnDeleteJugadorClick -> {
                viewModelScope.launch {
                    try {
                        repository.deleteJugador(event.jugador.jugadorId)
                        event.onSuccess()

                    } catch (e: Exception) {
                        println("Error al eliminar jugador: ${e.message}")
                    }
                }
            }
        }
    }
}
