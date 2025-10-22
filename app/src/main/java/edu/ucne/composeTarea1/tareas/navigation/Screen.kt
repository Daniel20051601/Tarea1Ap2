package edu.ucne.composeTarea1.tareas.navigation

sealed class Screen(val route: String) {
    data object ListJugador : Screen("list_jugador_screen")
    data object Partida : Screen("partida_screen")
    data object Historial : Screen("historial_partidas_screen")
    data object EditJugador : Screen("edit_jugador_screen/{jugadorId}") {
        const val ARG = "jugadorId"
        fun createRoute(jugadorId: Int) = "edit_jugador_screen/$jugadorId"
    }
    data object ListLogro: Screen("list_logro_screen")

    data object EditLogro: Screen("edit_logro_screen/{logroId}") {
        const val ARG = "logroId"
        fun createRoute(logroId: Int) = "edit_logro_screen/$logroId"
    }
}
