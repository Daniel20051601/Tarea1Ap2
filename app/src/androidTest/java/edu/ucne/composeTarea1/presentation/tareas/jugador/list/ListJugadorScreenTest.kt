package edu.ucne.composeTarea1.presentation.tareas.jugador.list

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.tareas.Presentation.Jugador.list.JugadorItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListJugadorScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun muestraJugadoresEnPantalla() {
        val jugadores = listOf(
            Jugador(1, "Juan Pérez", 5),
            Jugador(2, "Pedro Gómez", 3),
            Jugador(3, "Ana López", 7)
        )
        composeTestRule.setContent {
            Column {
                jugadores.forEach { jugador ->
                    JugadorItem(
                        jugador = jugador,
                        onJugadorClick = {},
                        onDeleteClick = {}
                    )
                }
            }
        }
        jugadores.forEach { jugador ->
            composeTestRule.onNodeWithText(jugador.nombres).assertExists()
        }
    }
}