package edu.ucne.composeTarea1.tareas.Presentation.Logro.edit

interface EditLogroEvent {
    data class Load(val id: Int?): EditLogroEvent
    data class NombreChanged(val nombre: String): EditLogroEvent
    data class DescripcionChanged(val descripcion: String): EditLogroEvent
    data object Save: EditLogroEvent
    data object Delete: EditLogroEvent
}