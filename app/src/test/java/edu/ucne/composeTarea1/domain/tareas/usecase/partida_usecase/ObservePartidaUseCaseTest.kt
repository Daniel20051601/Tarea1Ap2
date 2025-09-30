package edu.ucne.composeTarea1.domain.tareas.usecase.partida_usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import edu.ucne.composeTarea1.domain.usecase.partidaUseCase.ObservePartidaUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ObservePartidaUseCaseTest {

    private lateinit var repository: PartidaRepository
    private lateinit var useCase: ObservePartidaUseCase
    private lateinit var shared: MutableSharedFlow<List<Partida>>

    @Before
    fun setUp() {
        repository = mockk()
        shared = MutableSharedFlow()
        every { repository.observeAll() } returns shared
        useCase = ObservePartidaUseCase(repository)
    }

    @Test
    fun forwardsRepositoryEmissions() = runTest {
        val partida1 = Partida(
            partidaId = 1,
            fecha = "2025-01-01 10:00",
            jugador1Id = 10,
            jugador2Id = 20,
            ganadorId = null,
            esFinalizada = false,
            boardState = List(9) { null },
            currentPlayer = "X"
        )
        val partida2 = Partida(
            partidaId = 2,
            fecha = "2025-01-02 11:00",
            jugador1Id = 30,
            jugador2Id = 40,
            ganadorId = 30,
            esFinalizada = true,
            boardState = List(9) { null },
            currentPlayer = "O"
        )

        val flow = useCase()

        flow.test {
            shared.emit(listOf(partida1))
            val first = awaitItem()
            assertThat(first).hasSize(1)
            assertThat(first[0].partidaId).isEqualTo(1)

            shared.emit(listOf(partida1, partida2))
            val second = awaitItem()
            assertThat(second).hasSize(2)
            assertThat(second[1].partidaId).isEqualTo(2)

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { repository.observeAll() }
    }
}