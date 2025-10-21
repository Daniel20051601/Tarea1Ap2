package edu.ucne.composeTarea1.data.tareas.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Logro
import edu.ucne.composeTarea1.tareas.local.Logro.LogroDao
import edu.ucne.composeTarea1.tareas.local.Logro.LogroEntity
import edu.ucne.composeTarea1.tareas.repository.LogroRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogroRepositoryImplTest {

    private lateinit var dao: LogroDao
    private lateinit var repository: LogroRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = LogroRepositoryImpl(dao)
    }

    @Test
    fun observeLogro_mapsEntitiesToDomain() = runTest {
        val shared = MutableSharedFlow<List<LogroEntity>>()
        every { dao.observeAll() } returns shared

        val job = launch {
            repository.observeLogro().test {
                shared.emit(listOf(LogroEntity(logroId = 1, nombre = "Logro A", descripcion = "Descripción A", isCompletado = false)))
                val first = awaitItem()
                assertThat(first).containsExactly(Logro(logroId = 1, nombre = "Logro A", descripcion = "Descripción A", isCompletado = false))

                shared.emit(
                    listOf(
                        LogroEntity(logroId = 2, nombre = "Logro B", descripcion = "Descripción B", isCompletado = true),
                        LogroEntity(logroId = 3, nombre = "Logro C", descripcion = "Descripción C", isCompletado = false)
                    )
                )
                val second = awaitItem()
                assertThat(second).containsExactly(
                    Logro(logroId = 2, nombre = "Logro B", descripcion = "Descripción B", isCompletado = true),
                    Logro(logroId = 3, nombre = "Logro C", descripcion = "Descripción C", isCompletado = false)
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
        job.join()
    }

    @Test
    fun getById_returnsMappedDomainModel_whenEntityExists() = runTest {
        coEvery { dao.getById(5) } returns LogroEntity(logroId = 5, nombre = "Logro X", descripcion = "Descripción X", isCompletado = true)

        val result = repository.getById(5)

        assertThat(result).isEqualTo(Logro(logroId = 5, nombre = "Logro X", descripcion = "Descripción X", isCompletado = true))
    }

    @Test
    fun getById_returnsNull_whenEntityMissing() = runTest {
        coEvery { dao.getById(42) } returns null

        val result = repository.getById(42)

        assertThat(result).isNull()
    }

    @Test
    fun upsertLogro_callsDaoWithMappedEntity_andReturnsLogroId() = runTest {
        coEvery { dao.upsert(any()) } returns 10L
        val logro = Logro(logroId = 10, nombre = "Nuevo Logro", descripcion = "Nueva descripción", isCompletado = false)

        val returnedId = repository.upsertLogro(logro)

        assertThat(returnedId).isEqualTo(10)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun save_callsDaoUpsert() = runTest {
        coEvery { dao.upsert(any()) } returns 8L
        val logro = Logro(logroId = 8, nombre = "Logro Save", descripcion = "Descripción save", isCompletado = true)

        repository.save(logro)

        coVerify { dao.upsert(any()) }
    }

    @Test
    fun deleteById_callsDaoDeleteById() = runTest {
        coEvery { dao.deleteById(12) } returns Unit

        repository.deleteById(12)

        coVerify { dao.deleteById(12) }
    }

    @Test
    fun getByNombre_returnsMappedDomainModel_whenLogroExists() = runTest {
        val nombre = "Logro Existente"
        coEvery { dao.getByNombre(nombre) } returns LogroEntity(logroId = 7, nombre = nombre, descripcion = "Descripción existente", isCompletado = false)

        val result = repository.getByNombre(nombre)

        assertThat(result).isEqualTo(Logro(logroId = 7, nombre = nombre, descripcion = "Descripción existente", isCompletado = false))
    }

    @Test
    fun getByNombre_returnsNull_whenLogroNotFound() = runTest {
        val nombre = "Logro Inexistente"
        coEvery { dao.getByNombre(nombre) } returns null

        val result = repository.getByNombre(nombre)

        assertThat(result).isNull()
    }
}
