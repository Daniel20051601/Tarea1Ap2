package edu.ucne.composeTarea1.tareas.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarms
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
) {
    data object ListJugador : BottomNavItem(Screen.ListJugador, "Jugadores", Icons.Filled.List)

    data object Partida : BottomNavItem(Screen.Partida, "Partida", Icons.Filled.SportsEsports)
    data object Historial : BottomNavItem(Screen.Historial, "Historial", Icons.Filled.History)
    data object Logros : BottomNavItem(Screen.ListLogro, "Logros", Icons.Filled.Star)

    companion object {
        val items = listOf(ListJugador, Partida, Historial, Logros)
    }
}
