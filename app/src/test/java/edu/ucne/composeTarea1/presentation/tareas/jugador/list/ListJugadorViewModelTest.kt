package edu.ucne.composeTarea1.presentation.tareas.jugador.list

import app.cash.turbine.test
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.tareas.Presentation.Jugador.list.ListJugadorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import io.mockk.mockk
import io.mockk.every
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After

@OptIn(ExperimentalCoroutinesApi::class)
class ListJugadorViewModelTest {

    private lateinit var repository: JugadorRepository
    private lateinit var viewModel: ListJugadorViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val jugadoresFlow = MutableStateFlow<List<Jugador>>(emptyList())

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        every { repository.observeJugador() } returns jugadoresFlow
        viewModel = ListJugadorViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `estado inicial es isLoading true y lista vacía`() = runTest(testDispatcher) {
        viewModel.state.test {
            val initial = awaitItem()
            assertEquals(true, initial.isLoading)
            assertEquals(emptyList<Jugador>(), initial.jugadores)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `al emitir jugadores, el estado se actualiza correctamente`() = runTest(testDispatcher) {
        val jugadores = listOf(Jugador(1, "Juan", partidas = 0), Jugador(2, "Pedro", partidas = 0))
        viewModel.state.test {
            awaitItem()
            jugadoresFlow.value = jugadores
            val updated = awaitItem()
            assertEquals(false, updated.isLoading)
            assertEquals(jugadores, updated.jugadores)
            cancelAndIgnoreRemainingEvents()
        }
    }
}