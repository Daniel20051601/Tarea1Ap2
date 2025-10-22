package edu.ucne.composeTarea1.domain.tareas.usecase.jugador_usecase

import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.DeleteJugadorUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteJugadorUseCaseTest {
    private lateinit var repository: JugadorRepository
    private lateinit var useCase: DeleteJugadorUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = DeleteJugadorUseCase(repository)
    }

    @Test
    fun `calls repository deleteJugador with id`() = runTest {
        coEvery { repository.deleteJugador(5) } just runs

        useCase(5)

        coVerify { repository.deleteJugador(5) }
    }
}
