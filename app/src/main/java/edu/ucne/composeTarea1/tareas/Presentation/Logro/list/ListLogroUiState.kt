package edu.ucne.composeTarea1.tareas.Presentation.Logro.list

import edu.ucne.composeTarea1.domain.model.Logro

data class ListLogroUiState(
    val logros: List<Logro> = emptyList(),
    val isLoading: Boolean = true,
)