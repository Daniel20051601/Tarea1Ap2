package edu.ucne.composeTarea1.tareas.Presentation.HistorialPartidas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistorialPartidasViewModel @Inject constructor(
    private val partidaRepository: PartidaRepository,
    private val jugadorRepository: JugadorRepository
) : ViewModel() {

    val state: StateFlow<HistorialPartidasUiState> =
        partidaRepository.observeAll()
            .map { partidasList ->
                val partidasConNombres = partidasList.map { partida ->
                    val nombreDelGanador = if (partida.ganadorId != null) {
                        jugadorRepository.getJugador(partida.ganadorId)?.nombres ?: "Jugador Desconocido"
                    } else {
                        null
                    }
                    PartidaConGanador(partida = partida, nombreGanador = nombreDelGanador)
                }
                HistorialPartidasUiState(partidas = partidasConNombres, isLoading = false)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = HistorialPartidasUiState(isLoading = true)
            )
}