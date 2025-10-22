package edu.ucne.composeTarea1.domain.tareas.usecase.partida_usecase

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import edu.ucne.composeTarea1.domain.usecase.partidaUseCase.GetPartidaEnProgresoUseCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class GetPartidaEnProgresoUseCaseTest {

    private lateinit var repository: PartidaRepository
    private lateinit var useCase: GetPartidaEnProgresoUseCase
    private lateinit var partida: Partida

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPartidaEnProgresoUseCase(repository)
        partida = Partida(
            partidaId = 5,
            fecha = "2025-09-01 10:00",
            jugador1Id = 1,
            jugador2Id = 2,
            ganadorId = null,
            esFinalizada = false,
            boardState = List(9) { null },
            currentPlayer = "X"
        )
    }

    @Test
    fun emitsNonNullPartida() = runTest {
        every { repository.getPartidaEnProgreso() } returns flowOf(partida)

        val result = useCase().first()

        assertThat(result).isEqualTo(partida)
        verify(exactly = 1) { repository.getPartidaEnProgreso() }
        confirmVerified(repository)
    }

    @Test
    fun emitsNullWhenNoGameInProgress() = runTest {
        every { repository.getPartidaEnProgreso() } returns flowOf(null)

        val result = useCase().first()

        assertThat(result).isNull()
        verify(exactly = 1) { repository.getPartidaEnProgreso() }
        confirmVerified(repository)
    }

    @Test
    fun emitsNullThenPartida() = runTest {
        every { repository.getPartidaEnProgreso() } returns flowOf(null, partida)

        val emissions = useCase().take(2).toList()

        assertThat(emissions).containsExactly(null, partida).inOrder()
        verify(exactly = 1) { repository.getPartidaEnProgreso() }
        confirmVerified(repository)
    }

    @Test
    fun doesNotTransformOriginalFlow() = runTest {
        every { repository.getPartidaEnProgreso() } returns flow {
            emit(partida.copy(partidaId = 10))
        }

        val result = useCase().first()

        assertThat(result?.partidaId).isEqualTo(10)
        verify(exactly = 1) { repository.getPartidaEnProgreso() }
        confirmVerified(repository)
    }
}