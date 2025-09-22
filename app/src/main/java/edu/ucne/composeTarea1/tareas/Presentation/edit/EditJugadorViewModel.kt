package edu.ucne.composeTarea1.tareas.Presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.DeleteJugadorUseCase
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.GetJugadorUseCase
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.UpsertJugadorUseCase
import edu.ucne.composeTarea1.domain.validation.JugadorValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditJugadorViewModel @Inject constructor(
    private val upsertJugadorUseCase: UpsertJugadorUseCase,
    private val getJugadorUseCase: GetJugadorUseCase,
    private val deleteJugadorUseCase: DeleteJugadorUseCase,
    private val jugadorValidator: JugadorValidator
): ViewModel() {
    private val _state = MutableStateFlow(EditJugadorUiState())
    val state: StateFlow<EditJugadorUiState> = _state.asStateFlow()

    fun onEvent(event: EditJugadorEvent) {
        when(event) {
            is EditJugadorEvent.Load -> onLoad(event.id)
            is EditJugadorEvent.NombreChanged -> _state.update {
                it.copy(nombre = event.nombre, nombreError = null)
            }
            is EditJugadorEvent.PartidasChanged -> _state.update {
                it.copy(partidas = event.partidas, partidasError = null)
            }
            EditJugadorEvent.Save -> onSave()
            EditJugadorEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update {
                it.copy(
                    isLoading = false,
                    jugadorId = null,
                    nombre = "",
                    partidas = "",
                    errorMessage = null,
                    isSaved = false,
                    isSaving = false,
                    isDeleting = false,
                    canBeDeleted = false
                )
            }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val jugador = getJugadorUseCase(id)
                _state.update {
                    if (jugador != null) {
                        it.copy(
                            jugadorId = jugador.jugadorId,
                            nombre = jugador.nombres,
                            partidas = jugador.partidas.toString(),
                            isLoading = false,
                            errorMessage = null,
                            canBeDeleted = true
                        )
                    } else {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Tarea con ID $id no encontrada.",
                            jugadorId = null,
                            nombre = "",
                            partidas = "",
                            canBeDeleted = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido al cargar la tarea.",
                        canBeDeleted = false
                    )
                }
            }
        }
    }

    private fun onSave() {
        val nombre = state.value.nombre
        val partidasStr = state.value.partidas
        val currentJugadorId = state.value.jugadorId

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, nombreError = null, partidasError = null, errorMessage = null) }
            val nombreBasicValidation = jugadorValidator.validateNombre(nombre)
            val partidasValidation = jugadorValidator.validatePartida(partidasStr)
            val nombreUnicoValidation = jugadorValidator.validateNombreUnico(nombre, currentJugadorId)

            if (!nombreBasicValidation.isValid || !partidasValidation.isValid || !nombreUnicoValidation.isValid) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        nombreError = nombreBasicValidation.error ?: nombreUnicoValidation.error,
                        partidasError = partidasValidation.error,
                        errorMessage = "Por favor, corrija los errores en el formulario."
                    )
                }
                return@launch
            }
            val id = state.value.jugadorId ?: 0
            val jugador = Jugador(
                jugadorId = id,
                nombres = nombre,
                partidas = partidasStr.toInt()
            )

            val result = upsertJugadorUseCase(jugador)
            result.onSuccess { newId ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        isSaved = true,
                        jugadorId = newId,
                        errorMessage = null
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Error desconocido al guardar el jugador."
                    )
                }
            }
        }
    }

    private fun onDelete() {
        val jugadorId = _state.value.jugadorId
        if (jugadorId == null || jugadorId == 0) {
            _state.update { it.copy(errorMessage = "No se puede eliminar un Jugador sin ID válido.") }
            return
        }

        _state.update { it.copy(isDeleting = true, isSaved = false, errorMessage = null) }

        viewModelScope.launch {
            try {
                deleteJugadorUseCase(jugadorId)
                _state.update {
                    it.copy(
                        isDeleting = false,
                        isSaved = true,
                        errorMessage = null,
                        jugadorId = null,
                        nombre = "",
                        partidas = "",
                        canBeDeleted = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = e.message ?: "Error desconocido al eliminar el Jugador."
                    )
                }
            }
        }
    }

}