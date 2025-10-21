package edu.ucne.composeTarea1.domain.tareas.usecase.logro_usecase

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Logro
import edu.ucne.composeTarea1.domain.repository.LogroRepository
import edu.ucne.composeTarea1.domain.usecase.LogroUseCase.GetLogroUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetLogroUseCaseTest {
    private lateinit var repository: LogroRepository
    private lateinit var useCase: GetLogroUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetLogroUseCase(repository)
    }

    @Test
    fun `returns logro when found`() = runTest {
        val logro = Logro(logroId = 1, nombre = "Logro 1", descripcion = "Desc", isCompletado = false)
        coEvery { repository.getById(1) } returns logro

        val result = useCase(1)

        assertThat(result).isEqualTo(logro)
    }

    @Test
    fun `returns null when logro not found`() = runTest {
        coEvery { repository.getById(2) } returns null

        val result = useCase(2)

        assertThat(result).isNull()
    }
}
