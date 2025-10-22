package edu.ucne.composeTarea1.tareas.Presentation.Logro.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.composeTarea1.domain.model.Logro

@Composable
fun EditLogroScreen(
    navController: NavController,
    logroId: Int?,
    viewModel: EditLogroViewModel = hiltViewModel()
) {
    LaunchedEffect(logroId) {
        viewModel.onEvent(EditLogroEvent.Load(logroId))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.popBackStack()
        }
    }
    EditLogroBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditLogroBody(
    state: EditLogroUiState,
    onEvent: (EditLogroEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    var logroParaEliminar by remember { mutableStateOf<Logro?>(null) }

    logroParaEliminar?.let { logro ->
        DeleteConfirmationDialog(
            logro = logro,
            onConfirm = {
                onEvent(EditLogroEvent.Delete)
                logroParaEliminar = null
            },
            onDismiss = { logroParaEliminar = null }
        )
    }

    val topText = if (state.canBeDeleted) "Editar Logro" else "Añadir Logro"

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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { onEvent(EditLogroEvent.NombreChanged(it)) },
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
                value = state.descripcion,
                onValueChange = { onEvent(EditLogroEvent.DescripcionChanged(it)) },
                label = { Text("Descripción") },
                isError = state.descripcionError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.descripcionError != null) {
                Text(
                    text = state.descripcionError,
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
                    onClick = { onEvent(EditLogroEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }

                if (state.canBeDeleted) {
                    OutlinedButton(
                        onClick = {
                            state.logroId?.let { id ->
                                logroParaEliminar = Logro(
                                    logroId = id,
                                    nombre = state.nombre,
                                    descripcion = state.descripcion,
                                    isCompletado = state.isCompletado
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
    logro: Logro,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Eliminar Logro") },
        text = { Text("¿Estás seguro que quieres eliminar el logro ${logro.nombre}?") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Confirmar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

class EditLogroUiStatePreviewProvider : PreviewParameterProvider<EditLogroUiState> {
    override val values = sequenceOf(
        EditLogroUiState(
            nombre = "",
            descripcion = "",
            canBeDeleted = false
        ),
        EditLogroUiState(
            logroId = 1,
            nombre = "Logro Ejemplo",
            descripcion = "Descripción de ejemplo",
            canBeDeleted = true
        ),
        EditLogroUiState(
            nombre = "Lo",
            descripcion = "",
            nombreError = "El nombre es muy corto.",
            descripcionError = "La descripción no puede estar vacía.",
            canBeDeleted = false
        ),
        EditLogroUiState(
            logroId = 2,
            nombre = "Logro Guardando",
            descripcion = "Guardando...",
            isSaving = true,
            canBeDeleted = true
        ),
        EditLogroUiState(
            logroId = 3,
            nombre = "Logro a eliminar",
            descripcion = "Eliminar este logro",
            isDeleting = true,
            canBeDeleted = true
        )
    )
}

@Preview(showSystemUi = true, showBackground = true, widthDp = 320)
@Composable
fun EditLogroBodyPreview(
    @PreviewParameter(EditLogroUiStatePreviewProvider::class) state: EditLogroUiState
) {
    MaterialTheme {
        EditLogroBody(
            state = state,
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun EditLogroBodyNewLogroPreview() {
    MaterialTheme {
        EditLogroBody(
            state = EditLogroUiState(
                nombre = "",
                descripcion = "",
                canBeDeleted = false,
                isSaving = false,
                isDeleting = false,
                nombreError = null,
                descripcionError = null
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}



