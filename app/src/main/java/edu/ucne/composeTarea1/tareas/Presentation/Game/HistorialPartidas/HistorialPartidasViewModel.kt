package edu.ucne.composeTarea1.tareas.Presentation.Game.HistorialPartidas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.composeTarea1.domain.model.Partida // Asegúrate que esta clase y su ruta sean correctas
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest // Importa mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistorialPartidasViewModel @Inject constructor(
    private val partidaRepository: PartidaRepository,
    private val jugadorRepository: JugadorRepository
) : ViewModel() {

    val state: StateFlow<HistorialPartidasUiState> =
        partidaRepository.observeAll()
            .mapLatest { partidasList ->
                val partidasConNombres = partidasList.map { partida ->
                    val nombreDelGanador = if (partida.ganadorId != null) {
                        try {
                            jugadorRepository.getJugador(partida.ganadorId)?.nombres ?: "Jugador Desconocido"
                        } catch (e: Exception) {
                            "Error al obtener nombre"
                        }
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
