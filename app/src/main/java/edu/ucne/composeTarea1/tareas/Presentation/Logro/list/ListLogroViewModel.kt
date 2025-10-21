package edu.ucne.composeTarea1.tareas.Presentation.Logro.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.composeTarea1.domain.repository.LogroRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ListLogroViewModel @Inject constructor(
    private val repository: LogroRepository,
): ViewModel(){
    val state: StateFlow<ListLogroUiState> =
        repository.observeLogro()
            .map { logros ->
                ListLogroUiState(
                    logros = logros,
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = ListLogroUiState(isLoading = true)
            )
}