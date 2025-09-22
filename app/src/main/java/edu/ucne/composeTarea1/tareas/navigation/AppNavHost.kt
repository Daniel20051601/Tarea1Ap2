package edu.ucne.composeTarea1.tareas.navigation

import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.composeTarea1.tareas.Presentation.list.ListJugadorScreen
import edu.ucne.composeTarea1.tareas.Presentation.Partida.PartidaScreen
import edu.ucne.composeTarea1.tareas.Presentation.HistorialPartidas.HistorialPartidasScreen
import edu.ucne.composeTarea1.tareas.Presentation.edit.EditJugadorScreen

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
