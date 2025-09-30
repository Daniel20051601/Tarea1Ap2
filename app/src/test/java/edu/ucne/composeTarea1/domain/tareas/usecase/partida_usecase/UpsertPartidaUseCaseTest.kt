package edu.ucne.composeTarea1.domain.tareas.usecase.partida_usecase

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import edu.ucne.composeTarea1.domain.usecase.partidaUseCase.UpsertPartidaUseCase
import edu.ucne.composeTarea1.tareas.Presentation.Game.Partida.Player
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class UpsertPartidaUseCaseTest {

    private lateinit var repository: PartidaRepository
    private lateinit var useCase: UpsertPartidaUseCase
    private lateinit var partida: Partida

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = UpsertPartidaUseCase(repository)
        val board = MutableList<Player?>(9) { null }.also {
            it[0] = Player.X
            it[4] = Player.O
        }
        partida = Partida(
            partidaId = 0,
            fecha = "2025-09-01 12:00",
            jugador1Id = 1,
            jugador2Id = 2,
            ganadorId = null,
            esFinalizada = false,
            boardState = board,
            currentPlayer = "X"
        )
    }

    @Test
    fun callsRepositoryUpsert() = runTest {
        coEvery { repository.upsert(partida) } returns Unit

        useCase(partida)

        coVerify(exactly = 1) { repository.upsert(partida) }
        confirmVerified(repository)
    }

    @Test
    fun propagatesException() = runTest {
        val error = IllegalStateException("DB error")
        coEvery { repository.upsert(partida) } throws error

        val thrown = try {
            useCase(partida)
            error("Se esperaba IllegalStateException")
        } catch (e: IllegalStateException) {
            e
        }

        assertThat(thrown).isSameInstanceAs(error)
        coVerify(exactly = 1) { repository.upsert(partida) }
        confirmVerified(repository)
    }
}