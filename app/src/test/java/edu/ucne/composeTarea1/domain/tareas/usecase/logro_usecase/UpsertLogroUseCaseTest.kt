package edu.ucne.composeTarea1.domain.tareas.usecase.logro_usecase

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Logro
import edu.ucne.composeTarea1.domain.repository.LogroRepository
import edu.ucne.composeTarea1.domain.usecase.LogroUseCase.UpsertLogroUseCase
import edu.ucne.composeTarea1.domain.validation.LogroValidator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class UpsertLogroUseCaseTest {
    private lateinit var repository: LogroRepository
    private lateinit var validator: LogroValidator
    private lateinit var useCase: UpsertLogroUseCase

    @Before
    fun setup() {
        repository = mockk()
        validator = mockk()
        useCase = UpsertLogroUseCase(repository, validator)
    }

    @Test
    fun `retorna error si el nombre es inválido`() = runTest {
        val logro = Logro(0, "", "Descripción válida", isCompletado = false)
        coEvery { validator.validateNombre(logro.nombre) } returns
                edu.ucne.composeTarea1.domain.validation.ValidationResult(false, "Nombre inválido")
        coEvery { validator.validateDescripcion(logro.descripcion) } returns
                edu.ucne.composeTarea1.domain.validation.ValidationResult(true)

        val result = useCase(logro)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
        coVerify(exactly = 0) { repository.upsertLogro(any()) }
    }

    @Test
    fun `retorna error si la descripción es inválida`() = runTest {
        val logro = Logro(0, "Nombre válido", "", isCompletado = false)
        coEvery { validator.validateNombre(logro.nombre) } returns
                edu.ucne.composeTarea1.domain.validation.ValidationResult(true)
        coEvery { validator.validateDescripcion(logro.descripcion) } returns
                edu.ucne.composeTarea1.domain.validation.ValidationResult(false, "Descripción inválida")

        val result = useCase(logro)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
        coVerify(exactly = 0) { repository.upsertLogro(any()) }
    }

    @Test
    fun `llama al repositorio si los datos son válidos`() = runTest {
        val logro = Logro(0, "Nombre válido", "Descripción válida", isCompletado = false)
        coEvery { validator.validateNombre(logro.nombre) } returns
                edu.ucne.composeTarea1.domain.validation.ValidationResult(true)
        coEvery { validator.validateDescripcion(logro.descripcion) } returns
                edu.ucne.composeTarea1.domain.validation.ValidationResult(true)
        coEvery { repository.upsertLogro(logro) } returns 1

        val result = useCase(logro)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(1)
        coVerify(exactly = 1) { repository.upsertLogro(logro) }
    }
}

