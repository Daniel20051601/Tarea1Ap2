package edu.ucne.composeTarea1.domain.tareas.usecase.partida_usecase

import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import edu.ucne.composeTarea1.domain.usecase.partidaUseCase.DeletePartidaUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith



@OptIn(ExperimentalCoroutinesApi::class)
class DeletePartidaUseCaseTest {

    private lateinit var repository: PartidaRepository
    private lateinit var useCase: DeletePartidaUseCase

    private lateinit var partida: Partida

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = DeletePartidaUseCase(repository)
        partida = Partida(
            partidaId = 1,
            fecha = "2025-09-01 10:00",
            jugador1Id = 10,
            jugador2Id = 20,
            ganadorId = null,
            esFinalizada = false,
            boardState = List(9) { null },
            currentPlayer = "X"
        )
    }

    @Test
    fun `invokes repository delete exactly once`() = runTest {
        useCase(partida)

        coVerify(exactly = 1) { repository.delete(partida) }
        confirmVerified(repository)
    }

    @Test
    fun `propagates repository exception`() = runTest {
        val error = IllegalStateException("DB error")
        coEvery { repository.delete(partida) } throws error

        val thrown = assertFailsWith<IllegalStateException> {
            useCase(partida)
        }
        assert(thrown === error)

        coVerify(exactly = 1) { repository.delete(partida) }
        confirmVerified(repository)
    }
}