package edu.ucne.composeTarea1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector // Importar para ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.composeTarea1.tareas.Presentation.HistorialPartidas.HistorialPartidasScreen
import edu.ucne.composeTarea1.tareas.Presentation.Partida.PartidaScreen
import edu.ucne.composeTarea1.tareas.Presentation.edit.EditJugadorScreen
import edu.ucne.composeTarea1.tareas.Presentation.list.ListJugadorScreen
import edu.ucne.composeTarea1.ui.theme.Tarea1Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tarea1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "list_jugador_screen",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("list_jugador_screen") {
                                ListJugadorScreen(navController = navController)
                            }
                            composable("partida_screen") {
                                PartidaScreen()
                            }
                            composable("historial_partidas_screen"){
                                HistorialPartidasScreen()
                            }
                            composable(
                                route = "edit_jugador_screen/{jugadorId}",
                                arguments = listOf(
                                    navArgument("jugadorId") {
                                        type = NavType.IntType
                                    }
                                )
                            ) { backStackEntry ->
                                val jugadorId = backStackEntry.arguments?.getInt("jugadorId")
                                EditJugadorScreen(
                                    navController = navController,
                                    jugadorId = jugadorId
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object ListJugador : BottomNavItem("list_jugador_screen", "Jugadores", Icons.Default.List)
    object Partida : BottomNavItem("partida_screen", "Partida", Icons.Default.SportsEsports)
    object Historial: BottomNavItem("historial_partidas_screen","Historial",Icons.Default.History)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.ListJugador,
        BottomNavItem.Partida,
        BottomNavItem.Historial
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.title) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    Tarea1Theme {
        BottomNavigationBar(navController = rememberNavController())
    }

}