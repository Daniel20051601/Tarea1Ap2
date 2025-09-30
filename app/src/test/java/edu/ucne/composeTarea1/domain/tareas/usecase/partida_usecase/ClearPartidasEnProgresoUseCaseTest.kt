package edu.ucne.composeTarea1.domain.tareas.usecase.partida_usecase

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import edu.ucne.composeTarea1.domain.usecase.partidaUseCase.ClearPartidasEnProgresoUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class ClearPartidasEnProgresoUseCaseTest {

    private lateinit var repository: PartidaRepository
    private lateinit var useCase: ClearPartidasEnProgresoUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = ClearPartidasEnProgresoUseCase(repository)
    }

    @Test
    fun invokesRepositoryExactlyOnce() = runTest {
        useCase()
        coVerify(exactly = 1) { repository.clearPartidasEnProgreso() }
    }

    @Test
    fun propagatesRepositoryException() = runTest {
        val error = IllegalStateException("DB error")
        coEvery { repository.clearPartidasEnProgreso() } throws error
        val thrown = assertFailsWith<IllegalStateException> { useCase() }
        assertThat(thrown === error).isTrue()
        coVerify(exactly = 1) { repository.clearPartidasEnProgreso() }
    }
}
