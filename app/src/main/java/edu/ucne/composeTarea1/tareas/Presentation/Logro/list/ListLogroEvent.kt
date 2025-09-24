package edu.ucne.composeTarea1.tareas.Presentation.Logro.list

interface ListLogroEvent {
    data class OnDeleteLogroClick(val logroId: Int): ListLogroEvent
}