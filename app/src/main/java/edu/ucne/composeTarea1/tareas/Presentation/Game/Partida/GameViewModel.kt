package edu.ucne.composeTarea1.tareas.Presentation.Game.Partida

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.GetJugadorUseCase
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.ObserveJugadorUseCase
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.UpsertJugadorUseCase
import edu.ucne.composeTarea1.domain.usecase.partidaUseCase.ClearPartidasEnProgresoUseCase
import edu.ucne.composeTarea1.domain.usecase.partidaUseCase.GetPartidaEnProgresoUseCase
import edu.ucne.composeTarea1.domain.usecase.partidaUseCase.UpsertPartidaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getJugadorUseCase: GetJugadorUseCase,
    private val observeJugadorUseCase: ObserveJugadorUseCase,
    private val upsertJugadorUseCase: UpsertJugadorUseCase,
    private val getPartidaEnProgresoUseCase: GetPartidaEnProgresoUseCase,
    private val upsertPartidaUseCase: UpsertPartidaUseCase,
    private val clearPartidasEnProgresoUseCase: ClearPartidasEnProgresoUseCase
) : ViewModel() {

    private val _setupState = MutableStateFlow(PartidaSetupState())
    val setupState = _setupState.asStateFlow()

    private val _gameState = MutableStateFlow(GameUiState())
    val gameState = _gameState.asStateFlow()

    private val _isGameStarted = MutableStateFlow(false)
    val isGameStarted = _isGameStarted.asStateFlow()

    init {
        loadJugadores()
        loadGameInProgress()
    }

    private fun loadGameInProgress() {
        viewModelScope.launch {
            getPartidaEnProgresoUseCase().collect { partida ->
                if (partida != null && partida.jugador1Id != null && partida.jugador2Id != null) {
                    val jugador1 = getJugadorUseCase(partida.jugador1Id)
                    val jugador2 = getJugadorUseCase(partida.jugador2Id)
                    if (jugador1 != null && jugador2 != null) {
                        _setupState.update {
                            it.copy(
                                jugadorIzquierda = jugador1,
                                jugadorDerecha = jugador2
                            )
                        }
                        _gameState.update {
                            it.copy(
                                board = partida.boardState,
                                currentPlayer = if (partida.currentPlayer == "X") Player.X else Player.O
                            )
                        }
                        _isGameStarted.value = true
                    }
                }
            }
        }
    }

    private fun loadJugadores() {
        viewModelScope.launch {
            observeJugadorUseCase().collect { jugadores ->
                val selIzqId = _setupState.value.jugadorIzquierda?.jugadorId
                val selDerId = _setupState.value.jugadorDerecha?.jugadorId
                val izq = jugadores.find { it.jugadorId == selIzqId }
                val der = jugadores.find { it.jugadorId == selDerId }?.takeIf { it.jugadorId != izq?.jugadorId }
                _setupState.update {
                    it.copy(
                        jugadorIzquierda = izq,
                        jugadorDerecha = der,
                        listaOponentes = jugadores,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onJugadorIzquierdaSelected(jugador: Jugador) {
        _setupState.update { prev ->
            val derAjustado = prev.jugadorDerecha?.takeIf { it.jugadorId != jugador.jugadorId }
            prev.copy(jugadorIzquierda = jugador, jugadorDerecha = derAjustado)
        }
    }

    fun onJugadorDerechaSelected(jugador: Jugador) {
        val izq = _setupState.value.jugadorIzquierda
        if (izq == null || izq.jugadorId != jugador.jugadorId) {
            _setupState.update { it.copy(jugadorDerecha = jugador) }
        }
    }

    fun startGame() {
        val izq = _setupState.value.jugadorIzquierda
        val der = _setupState.value.jugadorDerecha
        if (izq != null && der != null && izq.jugadorId != der.jugadorId) {
            viewModelScope.launch {
                clearPartidasEnProgresoUseCase()
                val initialState = GameUiState()
                val nuevaPartida = Partida(
                    fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    jugador1Id = izq.jugadorId,
                    jugador2Id = der.jugadorId,
                    ganadorId = null,
                    esFinalizada = false,
                    boardState = initialState.board,
                    currentPlayer = initialState.currentPlayer.name
                )
                upsertPartidaUseCase(nuevaPartida)
                _gameState.value = initialState
                _isGameStarted.value = true
            }
        }
    }

    fun onCellClick(index: Int) {
        val state = _gameState.value
        if (state.board[index] != null || state.winner != null || state.isDraw) return
        val newBoard = state.board.toMutableList()
        newBoard[index] = state.currentPlayer
        val newWinner = checkWinner(newBoard)
        val isDraw = newBoard.all { it != null } && newWinner == null
        val nextPlayer = if (state.currentPlayer == Player.X) Player.O else Player.X
        _gameState.update {
            it.copy(
                board = newBoard,
                currentPlayer = nextPlayer,
                winner = newWinner,
                isDraw = isDraw
            )
        }
        saveGameProgress(newBoard, nextPlayer)
        if (newWinner != null || isDraw) {
            onGameFinished(newWinner, isDraw)
        }
    }

    private fun saveGameProgress(board: List<Player?>, currentPlayer: Player) {
        viewModelScope.launch {
            getPartidaEnProgresoUseCase().firstOrNull()?.let { partidaActual ->
                val partidaActualizada = partidaActual.copy(
                    boardState = board,
                    currentPlayer = currentPlayer.name
                )
                upsertPartidaUseCase(partidaActualizada)
            }
        }
    }

    fun restartGame() {
        viewModelScope.launch {
            clearPartidasEnProgresoUseCase()
            _gameState.value = GameUiState()
            _isGameStarted.value = false
            _setupState.update { it.copy(jugadorIzquierda = null, jugadorDerecha = null) }
        }
    }

    private fun onGameFinished(ganador: Player?, isDraw: Boolean) {
        viewModelScope.launch {
            val izq = setupState.value.jugadorIzquierda ?: return@launch
            val der = setupState.value.jugadorDerecha ?: return@launch
            val jugadorGanadorId: Int? = if (ganador != null) {
                val jugadorQueGano = if (ganador == Player.X) izq else der
                val actualizado = jugadorQueGano.copy(partidas = jugadorQueGano.partidas + 1)
                upsertJugadorUseCase(actualizado)
                jugadorQueGano.jugadorId
            } else null
            if (ganador != null || isDraw) {
                getPartidaEnProgresoUseCase().firstOrNull()?.let { partidaActual ->
                    val partidaFinalizada = partidaActual.copy(
                        ganadorId = jugadorGanadorId,
                        esFinalizada = true
                    )
                    upsertPartidaUseCase(partidaFinalizada)
                }
            }
        }
    }

    private fun checkWinner(board: List<Player?>): Player? {
        val lines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for ((a, b, c) in lines) {
            val va = board[a]
            if (va != null && va == board[b] && va == board[c]) return va
        }
        return null
    }
}
