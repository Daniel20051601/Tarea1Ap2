package edu.ucne.composeTarea1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.composeTarea1.tareas.edit.EditJugadorScreen
import edu.ucne.composeTarea1.tareas.list.ListJugadorScreen
import edu.ucne.composeTarea1.ui.theme.Tarea1Theme
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tarea1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "list_jugador_screen"
                    ) {
                        composable("list_jugador_screen") {
                            ListJugadorScreen(navController = navController)
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