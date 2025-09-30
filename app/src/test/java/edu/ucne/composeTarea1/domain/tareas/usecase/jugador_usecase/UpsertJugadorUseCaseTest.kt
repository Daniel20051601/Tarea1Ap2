package edu.ucne.composeTarea1.domain.tareas.usecase.jugador_usecase

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.UpsertJugadorUseCase
import edu.ucne.composeTarea1.domain.validation.JugadorValidator
import edu.ucne.composeTarea1.domain.validation.ValidationResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpsertJugadorUseCaseTest {
    private lateinit var repository: JugadorRepository
    private lateinit var validator: JugadorValidator
    private lateinit var useCase: UpsertJugadorUseCase

    @Before
    fun setup() {
        repository = mockk()
        validator = mockk()
        useCase = UpsertJugadorUseCase(repository, validator)
    }

    @Test
    fun `fails when name is invalid`() = runTest {
        coEvery { validator.validateNombre(any()) } returns ValidationResult(false, "Invalid name")
        coEvery { validator.validatePartida(any()) } returns ValidationResult(true)
        val jugador = Jugador(jugadorId = 0, nombres = "", partidas = 5)

        val result = useCase(jugador)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `fails when partidas is invalid`() = runTest {
        coEvery { validator.validateNombre(any()) } returns ValidationResult(true)
        coEvery { validator.validatePartida(any()) } returns ValidationResult(false, "Invalid partidas")
        val jugador = Jugador(jugadorId = 0, nombres = "Valid", partidas = 0)

        val result = useCase(jugador)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `succeeds and returns id when repository upsert works`() = runTest {
        coEvery { validator.validateNombre(any()) } returns ValidationResult(true)
        coEvery { validator.validatePartida(any()) } returns ValidationResult(true)
        coEvery { repository.upsertJugador(any()) } returns 10
        val jugador = Jugador(jugadorId = 10, nombres = "Valid", partidas = 3)

        val result = useCase(jugador)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(10)
    }
}
