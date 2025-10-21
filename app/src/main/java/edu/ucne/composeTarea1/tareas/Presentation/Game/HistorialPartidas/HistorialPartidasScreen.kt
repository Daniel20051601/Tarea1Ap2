package edu.ucne.composeTarea1.tareas.Presentation.Game.HistorialPartidas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.tareas.Presentation.Game.Partida.Player
import edu.ucne.composeTarea1.ui.theme.Tarea1Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialPartidasScreen(
    navigation: NavController,
    viewModel: HistorialPartidasViewModel = hiltViewModel(),

) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Partidas") },
                windowInsets = WindowInsets(top = 0.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigation.navigate("partida_screen")},
            ){
                Icon(Icons.Default.Add, contentDescription = "Nueva partida")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.partidas.isEmpty()) {
                Text("No hay partidas registradas.", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.partidas, key = { partidaConGanador -> partidaConGanador.partida.partidaId }) { partidaConGanador ->
                        PartidaItem(
                            partidaConGanador = partidaConGanador,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PartidaItem(
    partidaConGanador: PartidaConGanador,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Partida #${partidaConGanador.partida.partidaId}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Fecha: ${partidaConGanador.partida.fecha}")
                Spacer(modifier = Modifier.height(4.dp))

                val resultado = when {
                    partidaConGanador.nombreGanador != null -> "Ganador: ${partidaConGanador.nombreGanador}"
                    partidaConGanador.partida.esFinalizada -> "Resultado: Empate"
                    else -> "Partida en curso"
                }

                Text(
                    text = resultado,
                    color = if (partidaConGanador.nombreGanador != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PartidaItemPreview() {
    Tarea1Theme {
        PartidaItem(
            partidaConGanador = PartidaConGanador(
                partida = Partida(
                    partidaId = 1,
                    fecha = "2024-05-21",
                    jugador1Id = 1,
                    jugador2Id = 20,
                    ganadorId = 1,
                    esFinalizada = true,
                    boardState = listOf(
                        Player.X, Player.X, Player.X,
                        Player.O, Player.O, null,
                        null, null, null
                    ),
                    currentPlayer = "O"
                ),
                nombreGanador = "Juan Perez"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PartidaItemEmpatePreview() {
    PartidaItem(
        partidaConGanador = PartidaConGanador(
            partida = Partida(
                partidaId = 2,
                fecha = "2024-05-20",
                jugador1Id = 15,
                jugador2Id = 25,
                ganadorId = null,
                esFinalizada = true,
                boardState = listOf(
                    Player.X, Player.O, Player.X,
                    Player.O, Player.X, Player.X,
                    Player.O, Player.X, Player.O
                ),
                currentPlayer = "X"
            ),
            nombreGanador = null
        )
    )
}