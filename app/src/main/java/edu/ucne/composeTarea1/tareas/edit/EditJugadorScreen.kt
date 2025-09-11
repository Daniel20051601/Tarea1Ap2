package edu.ucne.composeTarea1.tareas.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun EditJugadorScreen(
    navController: NavController,
    jugadorId: Int?,
    viewModel: EditJugadorViewModel = hiltViewModel()
) {
    LaunchedEffect(jugadorId) {
        viewModel.onEvent(EditJugadorEvent.Load(jugadorId))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.popBackStack()
        }
    }
    EditJugadorBody(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun EditJugadorBody(
    state: EditJugadorUiState,
    onEvent: (EditJugadorEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { onEvent(EditJugadorEvent.NombreChanged(it)) },
                label = { Text("Nombre") },
                isError = state.nombreError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.nombreError != null) {
                Text(
                    text = state.nombreError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = state.partidas,
                onValueChange = { onEvent(EditJugadorEvent.PartidasChanged(it)) },
                label = { Text("Partidas") },
                isError = state.partidasError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            if (state.partidasError != null) {
                Text(
                    text = state.partidasError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onEvent(EditJugadorEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }

                if (state.canBeDeleted) {
                    OutlinedButton(
                        onClick = { onEvent(EditJugadorEvent.Delete) },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

class EditTaskUiStatePreviewProvider : PreviewParameterProvider<EditJugadorUiState> {
    override val values = sequenceOf(
        EditJugadorUiState(
            nombre = "",
            partidas = "",
            canBeDeleted = false
        ),
        EditJugadorUiState(
            jugadorId = 1,
            nombre = "Miguel Manuel",
            partidas = "4",
            canBeDeleted = true
        ),
        EditJugadorUiState(
            nombre = "ir",
            partidas = "0",
            nombreError = "El nombre es muy corto.",
            partidasError = "Las partidas deben ser un valor positivo.",
            canBeDeleted = false
        ),
        EditJugadorUiState(
            jugadorId = 1,
            nombre = "Jose Angel",
            partidas = "6",
            isSaving = true,
            canBeDeleted = true
        ),
        EditJugadorUiState(
            jugadorId = 1,
            nombre = "Jugador a eliminar",
            partidas = "15",
            isDeleting = true,
            canBeDeleted = true
        )
    )
}

@Preview(showSystemUi = true,showBackground = true, widthDp = 320)
@Composable
fun EditTaskBodyPreview(
    @PreviewParameter(EditTaskUiStatePreviewProvider::class) state: EditJugadorUiState
) {
    MaterialTheme {
        EditJugadorBody(
            state = state,
            onEvent = { event ->
                println("Preview Event: $event")
            }
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 320)
@Composable
fun EditTaskBodyNewTaskPreview() {
    MaterialTheme {
        EditJugadorBody(
            state = EditJugadorUiState(
                nombre = "",
                partidas = "",
                canBeDeleted = false,
                isSaving = false,
                isDeleting = false,
                nombreError = null,
                partidasError = null
            ),
            onEvent = {}
        )
    }
}