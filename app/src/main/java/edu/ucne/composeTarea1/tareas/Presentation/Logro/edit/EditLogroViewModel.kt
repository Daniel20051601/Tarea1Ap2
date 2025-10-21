package edu.ucne.composeTarea1.tareas.Presentation.Logro.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.composeTarea1.domain.model.Logro
import edu.ucne.composeTarea1.domain.usecase.LogroUseCase.DeleteLogroUseCase
import edu.ucne.composeTarea1.domain.usecase.LogroUseCase.GetLogroUseCase
import edu.ucne.composeTarea1.domain.usecase.LogroUseCase.UpsertLogroUseCase
import edu.ucne.composeTarea1.domain.validation.LogroValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditLogroViewModel @Inject constructor(
    private val upsertLogroUseCase: UpsertLogroUseCase,
    private val getLogroUseCase: GetLogroUseCase,
    private val deleteLogroUseCase: DeleteLogroUseCase,
    private val logroValidator: LogroValidator
): ViewModel() {
    private val _state = MutableStateFlow(EditLogroUiState())
    val state: StateFlow<EditLogroUiState> = _state.asStateFlow()

    fun onEvent(event: EditLogroEvent){
        when(event){
            is EditLogroEvent.Load -> onLoad(event.id)
            is EditLogroEvent.NombreChanged -> _state.update {
                it.copy(
                    nombre = event.nombre,
                    nombreError = null
                )
            }
            is EditLogroEvent.DescripcionChanged -> _state.update{
                it.copy(
                    descripcion = event.descripcion,
                    descripcionError = null
                )
            }
            EditLogroEvent.Save -> onSave()
            EditLogroEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if(id == null || id == 0){
            _state.update {
                it.copy(
                    isLoading = false,
                    logroId = null,
                    nombre = "",
                    descripcion = "",
                    errorMessage = null,
                    isSaved = false,
                    isSaving = false,
                    isDeleting = false,
                    canBeDeleted = false,
                )
            }
            return
        }

        _state.update {it.copy(isLoading = true, errorMessage = null)}

        viewModelScope.launch {
            try {
                val logro = getLogroUseCase(id)
                _state.update{
                    if(logro != null){
                        it.copy(
                            logroId = logro.logroId,
                            nombre = logro.nombre,
                            descripcion = logro.descripcion,
                            isLoading = false,
                            errorMessage = null,
                            canBeDeleted = true
                        )
                    }else{
                        it.copy(
                            isLoading = false,
                            errorMessage = "Logro no encontrado",
                            logroId = null,
                            nombre = "",
                            descripcion = "",
                            canBeDeleted = false
                        )
                    }
                }
            }catch (e: Exception){
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido",
                        canBeDeleted = false
                    )
                }
            }
        }
    }

    private fun onSave(){
        val nombre = state.value.nombre
        val descripcion = state.value.descripcion
        val currentLogroId = state.value.logroId

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, nombreError = null, descripcionError = null, errorMessage = null) }
            val nombreValidation = logroValidator.validateNombre(nombre)
            val descripcionValidation = logroValidator.validateDescripcion(descripcion)
            val nombreUnicoValidation = logroValidator.validateNombreUnico(nombre, currentLogroId)

            if(!nombreValidation.isValid || !descripcionValidation.isValid || !nombreUnicoValidation.isValid){
                _state.update {
                    it.copy(
                        isSaving = false,
                        nombreError = nombreValidation.error ?: nombreUnicoValidation.error,
                        descripcionError = descripcionValidation.error,
                        errorMessage = "Por favor, corrija los erres en el formulario"
                    )
                }
                return@launch
            }
            val id = state.value.logroId ?: 0
            val logro = Logro(
                logroId = id,
                nombre = nombre,
                descripcion = descripcion,
                isCompletado = false
            )

            val result = upsertLogroUseCase(logro)
            result.onSuccess { newId ->
                _state.update{
                    it.copy(
                        isSaving = false,
                        isSaved = true,
                        logroId = newId,
                        errorMessage = null
                    )
                }
            }.onFailure{e ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Error desconocido al guardar el logro"
                    )
                }

            }
        }
    }

    private fun onDelete(){
        val logroId = state.value.logroId
        if(logroId == null || logroId == 0){
            _state.update {it.copy(errorMessage = "No se puede eliminar un logro no guardado")}
            return
        }

        _state.update {it.copy(isDeleting =  true, isSaved = false, errorMessage = null)}

        viewModelScope.launch {
            try{
                deleteLogroUseCase(logroId)
                _state.update {
                    it.copy(
                        isDeleting = false,
                        isSaved = true,
                        errorMessage = null,
                        logroId = null,
                        nombre = "",
                        descripcion = "",
                        canBeDeleted = false
                    )
                }
            }catch (e: Exception){
                _state.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = e.message ?: "Error desconocido al eliminar el logro"
                    )
                }
            }
        }

    }



}