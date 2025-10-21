package edu.ucne.composeTarea1.domain.tareas.usecase.jugador_usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.ObserveJugadorUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ObserveJugadorUseCaseTest {
    private lateinit var repository: JugadorRepository
    private lateinit var useCase: ObserveJugadorUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = ObserveJugadorUseCase(repository)
    }

    @Test
    fun `emits lists from repository`() = runTest {
        val shared = MutableSharedFlow<List<Jugador>>()
        every { repository.observeJugador() } returns shared

        val job = launch {
            useCase().test {
                shared.emit(listOf(Jugador(jugadorId = 1, nombres = "Alice", partidas = 0)))
                assertThat(awaitItem()).containsExactly(Jugador(jugadorId = 1, nombres = "Alice", partidas = 0))

                shared.emit(listOf(Jugador(jugadorId = 2, nombres = "Bob", partidas = 0), Jugador(jugadorId = 3, nombres = "Carol", partidas = 0)))
                assertThat(awaitItem()).containsExactly(
                    Jugador(jugadorId = 2, nombres = "Bob", partidas = 0),
                    Jugador(jugadorId = 3, nombres = "Carol", partidas = 0)
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
        job.join()
    }
}
