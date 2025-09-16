package edu.ucne.composeTarea1.tareas.Presentation.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.tareas.Presentation.list.ListJugadorUiEvent

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
        onEvent = viewModel::onEvent,
        onNavigateBack = { navController.popBackStack() }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditJugadorBody(
    state: EditJugadorUiState,
    onEvent: (EditJugadorEvent) -> Unit,
    onNavigateBack: () -> Unit
) {

    var jugadorParaEliminar by remember { mutableStateOf<Jugador?>(null) }

    jugadorParaEliminar?.let { jugador ->
        DeleteConfirmationDialog(
            jugador = jugador,
            onConfirm = {
                onEvent(EditJugadorEvent.Delete)
                jugadorParaEliminar = null
            },
            onDismiss = {
                jugadorParaEliminar = null
            }
        )
    }


    var topText  =
        if (state.canBeDeleted){
            "Editar Jugador"
        }else {
            "Añadir Jugador"
        }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(topText) },
                windowInsets = WindowInsets(top = 0.dp),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
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
                        onClick = {
                            state.jugadorId?.let { id ->
                                jugadorParaEliminar = Jugador(
                                    jugadorId = id,
                                    nombres = state.nombre,
                                    partidas = state.partidas.toIntOrNull() ?: 0
                            )
                        }
                                  },
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

@Composable
fun DeleteConfirmationDialog(
    jugador: Jugador,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Eliminar Jugador")
        },
        text = {
            Text("¿Estás seguro de que quieres eliminar a ${jugador.nombres}?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )

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
            },
            onNavigateBack = {}
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
            onEvent = {},
            onNavigateBack = {}
        )
    }
}