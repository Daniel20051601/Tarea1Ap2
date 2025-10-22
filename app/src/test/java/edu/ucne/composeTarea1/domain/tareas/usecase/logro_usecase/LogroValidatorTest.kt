package edu.ucne.composeTarea1.domain.tareas.usecase.logro_usecase

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Logro
import edu.ucne.composeTarea1.domain.repository.LogroRepository
import edu.ucne.composeTarea1.domain.validation.LogroValidator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LogroValidatorTest {
    private lateinit var repository: LogroRepository
    private lateinit var validator: LogroValidator

    @Before
    fun setup() {
        repository = mockk()
        validator = LogroValidator(repository)
    }

    @Test
    fun `nombre vacío es inválido`() {
        val result = validator.validateNombre("")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("El nombre no puede estar vacío")
    }

    @Test
    fun `nombre muy corto es inválido`() {
        val result = validator.validateNombre("ab")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("El nombre debe tener al menos 3 caracteres")
    }

    @Test
    fun `nombre muy largo es inválido`() {
        val result = validator.validateNombre("a".repeat(51))
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("El nombre no puede tener más de 50 caracteres")
    }

    @Test
    fun `nombre válido pasa validación`() {
        val result = validator.validateNombre("Logro Ejemplo")
        assertThat(result.isValid).isTrue()
        assertThat(result.error).isNull()
    }

    @Test
    fun `descripción vacía es inválida`() {
        val result = validator.validateDescripcion("")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("La descripción no puede estar vacía")
    }

    @Test
    fun `descripción muy corta es inválida`() {
        val result = validator.validateDescripcion("Corta")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("La descripción debe tener al menos 10 caracteres")
    }

    @Test
    fun `descripción muy larga es inválida`() {
        val result = validator.validateDescripcion("a".repeat(201))
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("La descripción no puede tener más de 200 caracteres")
    }

    @Test
    fun `descripción válida pasa validación`() {
        val result = validator.validateDescripcion("Esta es una descripción válida.")
        assertThat(result.isValid).isTrue()
        assertThat(result.error).isNull()
    }

    @Test
    fun `nombre no único es inválido`() = runTest {
        val logroExistente = Logro(1, "Repetido", "desc", true)
        coEvery { repository.getByNombre("Repetido") } returns logroExistente

        val result = validator.validateNombreUnico("Repetido")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("Ya existe un logro con este nombre")
    }

    @Test
    fun `nombre único es válido`() = runTest {
        coEvery { repository.getByNombre("Único") } returns null

        val result = validator.validateNombreUnico("Único")
        assertThat(result.isValid).isTrue()
        assertThat(result.error).isNull()
    }

    @Test
    fun `nombre igual al del mismo logro es válido`() = runTest {
        val logroExistente = Logro(2, "Igual", "desc", isCompletado = true)
        coEvery { repository.getByNombre("Igual") } returns logroExistente

        val result = validator.validateNombreUnico("Igual", currentLogroId = 2)
        assertThat(result.isValid).isTrue()
        assertThat(result.error).isNull()
    }
}
