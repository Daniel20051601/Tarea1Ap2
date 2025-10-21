package edu.ucne.composeTarea1.tareas.Presentation.Jugador.edit

data class EditJugadorUiState(
    val jugadorId: Int? = null,
    val nombre: String = "",
    val partidas: String = "",
    val nombreError: String? = null,
    val partidasError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val canBeDeleted: Boolean = false
)
