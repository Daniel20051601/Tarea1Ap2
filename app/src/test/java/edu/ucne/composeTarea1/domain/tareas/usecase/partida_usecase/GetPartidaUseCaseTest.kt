package edu.ucne.composeTarea1.domain.tareas.usecase.partida_usecase

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import edu.ucne.composeTarea1.domain.usecase.partidaUseCase.GetPartidaUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPartidaUseCaseTest {

    private lateinit var repository: PartidaRepository
    private lateinit var useCase: GetPartidaUseCase
    private lateinit var partida: Partida

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPartidaUseCase(repository)
        partida = Partida(
            partidaId = 10,
            fecha = "2025-09-01 12:00",
            jugador1Id = 1,
            jugador2Id = 2,
            ganadorId = null,
            esFinalizada = false,
            boardState = List(9) { null },
            currentPlayer = "X"
        )
    }

    @Test
    fun returnsPartidaWhenExists() = runTest {
        coEvery { repository.getById(10) } returns partida

        val result = useCase(10)

        assertThat(result).isEqualTo(partida)
        coVerify(exactly = 1) { repository.getById(10) }
        confirmVerified(repository)
    }

    @Test
    fun returnsNullWhenNotFound() = runTest {
        coEvery { repository.getById(99) } returns null

        val result = useCase(99)

        assertThat(result).isNull()
        coVerify(exactly = 1) { repository.getById(99) }
        confirmVerified(repository)
    }

    @Test
    fun callsRepositoryWithCorrectId() = runTest {
        coEvery { repository.getById(any()) } returns null

        useCase(55)

        coVerify(exactly = 1) { repository.getById(55) }
        confirmVerified(repository)
    }

    @Test
    fun propagatesException() = runTest {
        val error = IllegalStateException("DB error")
        coEvery { repository.getById(10) } throws error

        val thrown = try {
            useCase(10)
            error("Se esperaba IllegalStateException")
        } catch (e: IllegalStateException) {
            e
        }

        assertThat(thrown).isSameInstanceAs(error)
        coVerify(exactly = 1) { repository.getById(10) }
        confirmVerified(repository)
    }
}
