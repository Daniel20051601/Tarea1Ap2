package edu.ucne.composeTarea1.domain.tareas.usecase.jugador_usecase

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.GetJugadorUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetJugadorUseCaseTest {
    private lateinit var repository: JugadorRepository
    private lateinit var useCase: GetJugadorUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetJugadorUseCase(repository)
    }

    @Test
    fun `returns player when repository finds it`() = runTest {
        val player = Jugador(jugadorId = 1, nombres = "John", partidas = 10)
        coEvery { repository.getJugador(1) } returns player

        val result = useCase(1)

        assertThat(result).isEqualTo(player)
    }

    @Test
    fun `returns null when repository returns null`() = runTest {
        coEvery { repository.getJugador(999) } returns null

        val result = useCase(999)

        assertThat(result).isNull()
    }
}
