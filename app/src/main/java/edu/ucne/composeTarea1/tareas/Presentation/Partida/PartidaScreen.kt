package edu.ucne.composeTarea1.tareas.Presentation.Partida

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.composeTarea1.R
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.ui.theme.Tarea1Theme
import kotlinx.coroutines.launch

@Composable
fun PartidaScreen(
    viewModel: GameViewModel = hiltViewModel()
) {
    val isGameStarted by viewModel.isGameStarted.collectAsStateWithLifecycle()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isGameStarted) {
                PartidaSetupScreen(viewModel = viewModel)
            } else {
                val gameState by viewModel.gameState.collectAsStateWithLifecycle()
                GameBoard(
                    uiState = gameState,
                    onCellClick = viewModel::onCellClick,
                    onRestartGame = viewModel::restartGame
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PartidaSetupScreen(viewModel: GameViewModel) {
    val state by viewModel.setupState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(stringResource(R.string.choose_opponent), fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerSlot(jugador = state.jugadorIzquierda, placeholder = stringResource(R.string.player_placeholder_you))
                Text(stringResource(R.string.vs), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                PlayerSlot(
                    jugador = state.jugadorDerecha,
                    placeholder = stringResource(R.string.player_placeholder_opponent),
                    onClick = { showBottomSheet = true }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { viewModel.startGame() },
                enabled = state.jugadorIzquierda != null && state.jugadorDerecha != null
            ) {
                Text(stringResource(R.string.start_game), fontSize = 18.sp)
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }, sheetState = sheetState) {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                item {
                    Text(
                        text = stringResource(R.string.select_an_opponent),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                if (state.listaOponentes.isEmpty()) {
                    item {
                        Text(stringResource(R.string.no_other_players))
                    }
                } else {
                    items(state.listaOponentes) { oponente ->
                        Text(
                            text = oponente.nombres,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onOponenteSelected(oponente)
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) showBottomSheet = false
                                    }
                                }
                                .padding(vertical = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerSlot(jugador: Jugador?, placeholder: String, onClick: (() -> Unit)? = null) {
    val modifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Card(modifier = modifier.size(120.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = jugador?.nombres ?: placeholder, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun GameBoard(
    uiState: GameUiState,
    onCellClick: (Int) -> Unit,
    onRestartGame: () -> Unit
) {
    val setupState by hiltViewModel<GameViewModel>().setupState.collectAsState()
    val jugadorX = setupState.jugadorIzquierda?.nombres ?: "Jugador X"
    val jugadorO = setupState.jugadorDerecha?.nombres ?: "Jugador O"

    val gameStatus = when {
        uiState.winner == Player.X -> stringResource(R.string.game_winner, jugadorX)
        uiState.winner == Player.O -> stringResource(R.string.game_winner, jugadorO)
        uiState.isDraw -> stringResource(R.string.game_draw)
        uiState.currentPlayer == Player.X -> stringResource(R.string.your_turn)
        else -> stringResource(R.string.opponent_turn, jugadorO)
    }
    Text(
        text = gameStatus,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(16.dp))
    if (uiState.winner == null && !uiState.isDraw) {
        val timerColor = if (uiState.remainingTime <= 10) Color.Red else MaterialTheme.colorScheme.onSurface
        Text(
            text = "Tiempo restante: ${uiState.remainingTime}s",
            fontSize = 18.sp,
            color = timerColor,
            fontWeight = FontWeight.SemiBold
        )
    }

    Spacer(modifier = Modifier.height(20.dp))
    GameBoardBody(board = uiState.board, onCellClick = onCellClick)
    Spacer(modifier = Modifier.height(20.dp))
    Button(onClick = onRestartGame) {
        Text(stringResource(R.string.restart_game), fontSize = 18.sp)
    }
}

@Composable
fun GameBoardBody(board: List<Player?>, onCellClick: (Int) -> Unit) {
    Column {
        (0..2).forEach { row ->
            Row {
                (0..2).forEach { col ->
                    val index = row * 3 + col
                    BoardCell(board[index]) {
                        onCellClick(index)
                    }
                }
            }
        }
    }
}

@Composable
private fun BoardCell(
    player: Player?,
    onCellClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .background(Color.LightGray)
            .clickable { onCellClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = player?.Symbol ?: "",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = if (player == Player.X) Color.Blue else Color.Red
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PartidaSetupScreenPreview() {
    Tarea1Theme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Elige a tu Oponente", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerSlot(jugador = Jugador(nombres = "Yo", partidas = 0), placeholder = "Yo")
                Text("VS", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                PlayerSlot(jugador = null, placeholder = "Oponente", onClick = {})
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {}, enabled = false) {
                Text("Iniciar Partida", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameBoardPreview() {
    Tarea1Theme {
        GameBoard(
            uiState = GameUiState(winner = Player.X),
            onCellClick = {},
            onRestartGame = {}
        )
    }
}