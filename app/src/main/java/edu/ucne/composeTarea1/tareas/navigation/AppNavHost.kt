package edu.ucne.composeTarea1.tareas.navigation

import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.composeTarea1.tareas.Presentation.Jugador.list.ListJugadorScreen
import edu.ucne.composeTarea1.tareas.Presentation.Game.Partida.PartidaScreen
import edu.ucne.composeTarea1.tareas.Presentation.Game.HistorialPartidas.HistorialPartidasScreen
import edu.ucne.composeTarea1.tareas.Presentation.Jugador.edit.EditJugadorScreen
import edu.ucne.composeTarea1.tareas.Presentation.Logro.edit.EditLogroScreen
import edu.ucne.composeTarea1.tareas.Presentation.Logro.list.ListLogroScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Partida.route,
        modifier = modifier
    ) {
        composable(Screen.ListJugador.route) {
            ListJugadorScreen(navController = navController)
        }
        composable(Screen.Partida.route) {
            PartidaScreen()
        }
        composable(Screen.Historial.route) {
            HistorialPartidasScreen(navigation = navController)
        }
        composable (Screen.ListLogro.route ){
            ListLogroScreen(navController = navController)
        }
        composable(
            route = Screen.EditLogro.route,
            arguments = listOf(
                navArgument(Screen.EditLogro.ARG) { type = NavType.IntType }
            )
        ){backStackEntry ->
            val logroId = backStackEntry.arguments?.getInt(Screen.EditLogro.ARG)
            EditLogroScreen(
                navController = navController,
                logroId = logroId
            )
        }
        composable(
            route = Screen.EditJugador.route,
            arguments = listOf(
                navArgument(Screen.EditJugador.ARG) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val jugadorId = backStackEntry.arguments?.getInt(Screen.EditJugador.ARG)
            EditJugadorScreen(
                navController = navController,
                jugadorId = jugadorId
            )
        }
    }
}
