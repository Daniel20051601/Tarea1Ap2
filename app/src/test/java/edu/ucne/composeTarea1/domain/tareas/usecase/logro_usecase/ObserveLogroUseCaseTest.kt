package edu.ucne.composeTarea1.domain.tareas.usecase.logro_usecase

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Logro
import edu.ucne.composeTarea1.domain.repository.LogroRepository
import edu.ucne.composeTarea1.domain.usecase.LogroUseCase.ObserveLogroUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ObserveLogroUseCaseTest {
    private lateinit var repository: LogroRepository
    private lateinit var useCase: ObserveLogroUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = ObserveLogroUseCase(repository)
    }

    @Test
    fun `retorna el flujo de logros del repositorio`() = runTest {
        val logros = listOf(
            Logro(1, "Logro 1", "Desc 1", isCompletado= true),
            Logro(2, "Logro 2", "Desc 2", isCompletado= true)
        )
        every { repository.observeLogro() } returns flowOf(logros)

        val result = useCase().first()

        assertThat(result).isEqualTo(logros)
    }
}
