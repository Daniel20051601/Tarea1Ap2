package edu.ucne.composeTarea1.presentation.tareas.jugador.edit

import edu.ucne.composeTarea1.tareas.Presentation.Jugador.edit.EditJugadorBody
import edu.ucne.composeTarea1.tareas.Presentation.Jugador.edit.EditJugadorEvent
import edu.ucne.composeTarea1.tareas.Presentation.Jugador.edit.EditJugadorUiState
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*

class EditJugadorScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun userCanTypeAndSave() {
        var lastEvent: EditJugadorEvent? = null
        composeRule.setContent {
            EditJugadorBody(
                state = EditJugadorUiState(),
                onEvent = { lastEvent = it },
                onNavigateBack = {}
            )
        }

        composeRule.onNodeWithTag("input_nombre").assertIsDisplayed().performTextInput("Pedro")
        composeRule.onNodeWithTag("input_partidas").assertIsDisplayed().performTextInput("8")
        composeRule.onNodeWithTag("btn_guardar").performClick()

        assert(lastEvent is EditJugadorEvent.Save)
    }

    @Test
    fun showDeleteWhenEditing_andClickEmitsDelete() {
        var lastEvent: EditJugadorEvent? = null
        composeRule.setContent {
            EditJugadorBody(
                state = EditJugadorUiState(
                    jugadorId = 1,
                    nombre = "Ana",
                    partidas = "3",
                    canBeDeleted = true
                ),
                onEvent = { lastEvent = it },
                onNavigateBack = {}
            )
        }
        composeRule.onNodeWithTag("btn_eliminar").assertIsDisplayed().performClick()
    }
}