package edu.ucne.composeTarea1.data.tareas.repository

import app.cash.turbine.test
import com.google.common.truth.Truth
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.tareas.local.Jugador.JugadorDao
import edu.ucne.composeTarea1.tareas.local.Jugador.JugadorEntity
import edu.ucne.composeTarea1.tareas.repository.JugadorRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class JugadorRepositoryImplTest {

    private lateinit var dao: JugadorDao
    private lateinit var repository: JugadorRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = JugadorRepositoryImpl(dao)
    }

    @Test
    fun observeJugador_mapsEntitiesToDomain() = runTest {
        val shared = MutableSharedFlow<List<JugadorEntity>>()
        every { dao.observeAll() } returns shared

        val job = launch {
            repository.observeJugador().test {
                // Primera emisión
                shared.emit(
                    listOf(
                        JugadorEntity(
                            jugadorId = 1,
                            nombres = "Jugador A",
                            partidas = 5
                        )
                    )
                )
                val first = awaitItem()
                Truth.assertThat(first)
                    .containsExactly(Jugador(jugadorId = 1, nombres = "Jugador A", partidas = 5))

                // Segunda emisión (múltiples elementos)
                shared.emit(
                    listOf(
                        JugadorEntity(jugadorId = 2, nombres = "Jugador B", partidas = 3),
                        JugadorEntity(jugadorId = 3, nombres = "Jugador C", partidas = 4)
                    )
                )
                val second = awaitItem()
                Truth.assertThat(second).containsExactly(
                    Jugador(jugadorId = 2, nombres = "Jugador B", partidas = 3),
                    Jugador(jugadorId = 3, nombres = "Jugador C", partidas = 4)
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
        job.join()
    }

    @Test
    fun getJugador_returnsMappedDomainModel_whenEntityExists() = runTest {
        coEvery { dao.getById(5) } returns JugadorEntity(
            jugadorId = 5,
            nombres = "Jugador X",
            partidas = 7
        )

        val result = repository.getJugador(5)

        Truth.assertThat(result)
            .isEqualTo(Jugador(jugadorId = 5, nombres = "Jugador X", partidas = 7))
    }

    @Test
    fun getJugador_returnsNull_whenEntityMissing() = runTest {
        coEvery { dao.getById(42) } returns null

        val result = repository.getJugador(42)

        Truth.assertThat(result).isNull()
    }

    @Test
    fun upsertJugador_callsDaoWithMappedEntity_andReturnsJugadorId() = runTest {
        coEvery { dao.upsert(any()) } returns 10L
        val jugador = Jugador(jugadorId = 10, nombres = "Nuevo Jugador", partidas = 1)

        val returnedId = repository.upsertJugador(jugador)

        Truth.assertThat(returnedId).isEqualTo(10)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun save_callsDaoUpsert() = runTest {
        coEvery { dao.upsert(any()) } returns 0L
        val jugador = Jugador(jugadorId = 8, nombres = "Jugador Save", partidas = 3)

        repository.save(jugador)

        coVerify { dao.upsert(any()) }
    }


    @Test
    fun deleteJugador_callsDaoDeleteById() = runTest {
        coEvery { dao.delete(12) } just runs

        repository.deleteJugador(12)

        coVerify { dao.delete(12) }
    }

    @Test
    fun getJugadorByName_returnsMappedDomainModel_whenJugadorExists() = runTest {
        val nombre = "Jugador Existente"
        val jugador = Jugador(jugadorId = 7, nombres = nombre, partidas = 2)
        coEvery { dao.getJugadorByName(nombre) } returns jugador

        val result = repository.getJugadorByName(nombre)

        Truth.assertThat(result).isEqualTo(jugador)
    }

    @Test
    fun getJugadorByName_returnsNull_whenJugadorNotFound() = runTest {
        val nombre = "Jugador Inexistente"
        coEvery { dao.getJugadorByName(nombre) } returns null

        val result = repository.getJugadorByName(nombre)

        Truth.assertThat(result).isNull()
    }
}