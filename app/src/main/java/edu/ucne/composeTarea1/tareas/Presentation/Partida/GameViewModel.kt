package edu.ucne.composeTarea1.tareas.Presentation.Partida

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class PartidaSetupState(
    val jugadorIzquierda: Jugador? = null,
    val jugadorDerecha: Jugador? = null,
    val listaOponentes: List<Jugador> = emptyList(),
    val isLoading: Boolean = true
)

data class GameUiState(
    val board: List<Player?> = List(9) { null },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val isDraw: Boolean = false,
    val remainingTime: Int = 30
)

enum class Player(val Symbol: String) {
    X("X"), O("O")
}

@HiltViewModel
class GameViewModel @Inject constructor(
    private val jugadorRepository: JugadorRepository,
    private val partidaRepository: PartidaRepository
) : ViewModel() {

    private val _setupState = MutableStateFlow(PartidaSetupState())
    val setupState = _setupState.asStateFlow()

    private val _gameState = MutableStateFlow(GameUiState())
    val gameState = _gameState.asStateFlow()
    private val _isGameStarted = MutableStateFlow(false)
    val isGameStarted = _isGameStarted.asStateFlow()
    private var timerJob: Job? = null
    private var lastTurnWasTimeout = false

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val yoPlayer = jugadorRepository.getJugadorByName("Yo")
            jugadorRepository.observeJugador().collect { jugadores ->
                _setupState.update {
                    it.copy(
                        jugadorIzquierda = yoPlayer,
                        listaOponentes = jugadores.filter { j -> j.nombres != "Yo" },
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onOponenteSelected(oponente: Jugador) {
        _setupState.update { it.copy(jugadorDerecha = oponente) }
    }

    fun startGame() {
        if (_setupState.value.jugadorIzquierda != null && _setupState.value.jugadorDerecha != null) {
            _isGameStarted.value = true
            startTurnTimer()
        }
    }

    private fun startTurnTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (time in 30 downTo 0) {
                _gameState.update { it.copy(remainingTime = time) }
                delay(1000L)
            }
            handleTimeout()
        }
    }

    private fun handleTimeout() {
        if (lastTurnWasTimeout) {
            _gameState.update { it.copy(isDraw = true, winner = null) }
            onGameFinished(null)
        } else {
            lastTurnWasTimeout = true
            val nextPlayer = if (_gameState.value.currentPlayer == Player.X) Player.O else Player.X
            _gameState.update { it.copy(currentPlayer = nextPlayer) }
            startTurnTimer()
        }
    }

    private fun onGameFinished(ganador: Player?) {
        timerJob?.cancel()
        viewModelScope.launch {
            val jugadorIzquierda = setupState.value.jugadorIzquierda ?: return@launch
            val jugadorDerecha = setupState.value.jugadorDerecha ?: return@launch

            val jugadorGanador = when (ganador) {
                Player.X -> jugadorIzquierda
                Player.O -> jugadorDerecha
                null -> null
            }

            if (jugadorGanador != null) {
                val ganadorActualizado = jugadorGanador.copy(partidas = jugadorGanador.partidas + 1)
                jugadorRepository.save(ganadorActualizado)
            }

            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val fechaActual = now.format(formatter)

            val partida = Partida(
                fecha = fechaActual,
                jugador1Id = jugadorIzquierda.jugadorId,
                jugador2Id = jugadorDerecha.jugadorId,
                ganadorId = jugadorGanador?.jugadorId,
                esFinalizada = true
            )
            partidaRepository.upsert(partida)
        }
    }

    fun onCellClick(index: Int) {
        if (_gameState.value.board[index] != null || _gameState.value.winner != null || _gameState.value.isDraw) {
            return
        }

        lastTurnWasTimeout = false

        val newBoard = _gameState.value.board.toMutableList()
        newBoard[index] = _gameState.value.currentPlayer
        val newWinner = checkWinner(newBoard)
        val isDraw = newBoard.all { it != null } && newWinner == null

        _gameState.update {
            it.copy(
                board = newBoard,
                currentPlayer = if (it.currentPlayer == Player.X) Player.O else Player.X,
                winner = newWinner,
                isDraw = isDraw
            )
        }

        if (newWinner != null || isDraw) {
            onGameFinished(newWinner)
        } else {
            startTurnTimer()
        }
    }

    fun restartGame() {
        timerJob?.cancel()
        lastTurnWasTimeout = false
        _gameState.value = GameUiState()
        _isGameStarted.value = false
    }

    private fun checkWinner(board: List<Player?>): Player? {
        val winningLines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for (line in winningLines) {
            val (a, b, c) = line
            if (board[a] != null && board[a] == board[b] && board[a] == board[c]) {
                return board[a]
            }
        }
        return null
    }
}