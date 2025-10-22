package edu.ucne.composeTarea1.domain.tareas.usecase.jugador_usecase

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.domain.validation.JugadorValidator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class JugadorValidatorTest {
    private lateinit var repository: JugadorRepository
    private lateinit var validator: JugadorValidator

    @Before
    fun setup() {
        repository = mockk()
        validator = JugadorValidator(repository)
    }

    @Test
    fun `invalid when name is blank`() {
        val result = validator.validateNombre("")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("Debe agregar el nombre del jugador.")
    }

    @Test
    fun `invalid when name is too short`() {
        val result = validator.validateNombre("Jo")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("El nombre es demasiado corto.")
    }

    @Test
    fun `invalid when name is too long`() {
        val longName = "a".repeat(101)
        val result = validator.validateNombre(longName)
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("El nombre no puede contener mas de 100 caracteres.")
    }

    @Test
    fun `valid name passes validation`() {
        val result = validator.validateNombre("Juan Perez")
        assertThat(result.isValid).isTrue()
        assertThat(result.error).isNull()
    }

    @Test
    fun `invalid when partidas is blank`() {
        val result = validator.validatePartida("")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("Debe agregar la(s) partida(s).")
    }

    @Test
    fun `invalid when partidas is not a number`() {
        val result = validator.validatePartida("abc")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("Debe ser un número entero.")
    }

    @Test
    fun `invalid when partidas is negative`() {
        val result = validator.validatePartida("-1")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("Debe ser un valor positivo.")
    }

    @Test
    fun `invalid when partidas is too high`() {
        val result = validator.validatePartida("101")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("El valor no puede ser mayor a 1000.")
    }

    @Test
    fun `valid partidas passes validation`() {
        val result = validator.validatePartida("10")
        assertThat(result.isValid).isTrue()
        assertThat(result.error).isNull()
    }

    @Test
    fun `invalid when name is not unique`() = runTest {
        coEvery { repository.getJugadorByName("Juan") } returns mockk()
        val result = validator.validateNombreUnico("Juan")
        assertThat(result.isValid).isFalse()
        assertThat(result.error).isEqualTo("Ya existe un jugador con este nombre.")
    }

    @Test
    fun `valid when name is unique`() = runTest {
        coEvery { repository.getJugadorByName("Pedro") } returns null
        val result = validator.validateNombreUnico("Pedro")
        assertThat(result.isValid).isTrue()
        assertThat(result.error).isNull()
    }
}
