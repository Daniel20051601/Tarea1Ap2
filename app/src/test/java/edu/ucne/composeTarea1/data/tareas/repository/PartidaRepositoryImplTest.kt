package edu.ucne.composeTarea1.data.tareas.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.tareas.Presentation.Game.Partida.Player
import edu.ucne.composeTarea1.tareas.local.Partida.PartidaDao
import edu.ucne.composeTarea1.tareas.local.Partida.PartidaEntity
import edu.ucne.composeTarea1.tareas.repository.PartidaRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PartidaRepositoryImplTest {

    private lateinit var dao: PartidaDao
    private lateinit var repository: PartidaRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = PartidaRepositoryImpl(dao)
    }

    @Test
    fun observeAll_mapsEntitiesToDomain() = runTest {
        val shared = MutableSharedFlow<List<PartidaEntity>>()
        every { dao.observeAll() } returns shared

        val job = launch {
            repository.observeAll().test {
                // Primera emisión
                shared.emit(listOf(
                    PartidaEntity(
                        partidaId = 1,
                        fecha = "2023-06-01",
                        jugador1Id = 10,
                        jugador2Id = 20,
                        ganadorId = 10,
                        esFinalizada = true,
                        boardState = "XXXOOOXXX",
                        currentPlayer = "X"
                    )
                ))

                val first = awaitItem()
                assertThat(first).hasSize(1)
                assertThat(first[0].partidaId).isEqualTo(1)
                assertThat(first[0].fecha).isEqualTo("2023-06-01")
                assertThat(first[0].jugador1Id).isEqualTo(10)
                assertThat(first[0].boardState).hasSize(9)  // Verificamos que el tablero tenga 9 posiciones
                assertThat(first[0].boardState[0]).isEqualTo(Player.X)  // Verificamos la primera celda

                // Segunda emisión (múltiples elementos)
                shared.emit(listOf(
                    PartidaEntity(
                        partidaId = 2,
                        fecha = "2023-06-02",
                        jugador1Id = 15,
                        jugador2Id = 25,
                        ganadorId = 25,
                        esFinalizada = true,
                        boardState = "OOXOXXOXO",
                        currentPlayer = "O"
                    ),
                    PartidaEntity(
                        partidaId = 3,
                        fecha = "2023-06-03",
                        jugador1Id = 30,
                        jugador2Id = 40,
                        ganadorId = null,
                        esFinalizada = false,
                        boardState = "XO.......",
                        currentPlayer = "X"
                    )
                ))

                val second = awaitItem()
                assertThat(second).hasSize(2)
                assertThat(second[0].partidaId).isEqualTo(2)
                assertThat(second[1].partidaId).isEqualTo(3)

                cancelAndIgnoreRemainingEvents()
            }
        }
        job.join()
    }

    @Test
    fun getById_returnsMappedDomainModel_whenEntityExists() = runTest {
        coEvery { dao.getById(5) } returns PartidaEntity(
            partidaId = 5,
            fecha = "2023-06-05",
            jugador1Id = 50,
            jugador2Id = 60,
            ganadorId = null,
            esFinalizada = false,
            boardState = "X....O...",
            currentPlayer = "O"
        )

        val result = repository.getById(5)

        assertThat(result).isNotNull()
        assertThat(result?.partidaId).isEqualTo(5)
        assertThat(result?.fecha).isEqualTo("2023-06-05")
        assertThat(result?.jugador1Id).isEqualTo(50)
        assertThat(result?.jugador2Id).isEqualTo(60)
        assertThat(result?.ganadorId).isNull()
        assertThat(result?.esFinalizada).isFalse()
        assertThat(result?.boardState).hasSize(9)
        assertThat(result?.boardState?.get(0)).isEqualTo(Player.X)
        assertThat(result?.boardState?.get(5)).isEqualTo(Player.O)
    }

    @Test
    fun getById_returnsNull_whenEntityMissing() = runTest {
        coEvery { dao.getById(42) } returns null

        val result = repository.getById(42)

        assertThat(result).isNull()
    }

    @Test
    fun upsert_callsDaoUpsert() = runTest {
        // Creamos un tablero que será mapeado a una string en el mapper toEntity
        val boardState = MutableList<Player?>(9) { null }
        boardState[0] = Player.X
        boardState[1] = Player.O
        boardState[2] = Player.X
        boardState[4] = Player.X
        boardState[7] = Player.O

        val partida = Partida(
            partidaId = 7,
            fecha = "2023-06-07",
            jugador1Id = 70,
            jugador2Id = 80,
            ganadorId = 70,
            esFinalizada = true,
            boardState = boardState,
            currentPlayer = "X"
        )

        repository.upsert(partida)

        coVerify { dao.upsert(any()) }
    }

    @Test
    fun delete_callsDaoDelete() = runTest {
        // Creamos un tablero que será mapeado a una string en el mapper toEntity
        val boardState = MutableList<Player?>(9) { null }
        boardState[0] = Player.O
        boardState[1] = Player.O
        boardState[2] = Player.X
        boardState[3] = Player.X
        boardState[4] = Player.X
        boardState[6] = Player.O

        val partida = Partida(
            partidaId = 8,
            fecha = "2023-06-08",
            jugador1Id = 75,
            jugador2Id = 85,
            ganadorId = 85,
            esFinalizada = true,
            boardState = boardState,
            currentPlayer = "O"
        )

        repository.delete(partida)

        coVerify { dao.delete(any()) }
    }

    @Test
    fun getPartidaEnProgreso_returnsMappedDomainModel_whenPartidaExists() = runTest {
        val partidaEntity = PartidaEntity(
            partidaId = 9,
            fecha = "2023-06-09",
            jugador1Id = 90,
            jugador2Id = 95,
            ganadorId = null,
            esFinalizada = false,
            boardState = "XO.......",
            currentPlayer = "X"
        )

        every { dao.getPartidaEnProgreso() } returns flowOf(partidaEntity)

        repository.getPartidaEnProgreso().test {
            val result = awaitItem()
            assertThat(result).isNotNull()
            assertThat(result?.partidaId).isEqualTo(9)
            assertThat(result?.esFinalizada).isFalse()
            assertThat(result?.boardState).hasSize(9)
            assertThat(result?.boardState?.get(0)).isEqualTo(Player.X)
            assertThat(result?.boardState?.get(1)).isEqualTo(Player.O)
            assertThat(result?.boardState?.get(2)).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getPartidaEnProgreso_returnsNull_whenNoPartidaEnProgreso() = runTest {
        every { dao.getPartidaEnProgreso() } returns flowOf(null)

        repository.getPartidaEnProgreso().test {
            val result = awaitItem()
            assertThat(result).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun clearPartidasEnProgreso_callsDaoClearPartidasEnProgreso() = runTest {
        repository.clearPartidasEnProgreso()

        coVerify { dao.clearPartidasEnProgreso() }
    }
}
