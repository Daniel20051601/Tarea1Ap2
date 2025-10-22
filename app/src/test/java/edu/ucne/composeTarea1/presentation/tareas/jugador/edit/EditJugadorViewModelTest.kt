package edu.ucne.composeTarea1.presentation.tareas.jugador.edit

import com.google.common.truth.Truth.assertThat
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.DeleteJugadorUseCase
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.GetJugadorUseCase
import edu.ucne.composeTarea1.domain.usecase.jugadorUseCase.UpsertJugadorUseCase
import edu.ucne.composeTarea1.domain.validation.JugadorValidator
import edu.ucne.composeTarea1.tareas.Presentation.Jugador.edit.EditJugadorEvent
import edu.ucne.composeTarea1.tareas.Presentation.Jugador.edit.EditJugadorViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class EditJugadorViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var upsertJugador: UpsertJugadorUseCase
    private lateinit var getJugador: GetJugadorUseCase
    private lateinit var deleteJugador: DeleteJugadorUseCase
    private lateinit var validator: JugadorValidator

    @Before
    fun setUp() {
        kotlinx.coroutines.Dispatchers.setMain(dispatcher)
        upsertJugador = mockk()
        getJugador = mockk()
        deleteJugador = mockk()
        validator = mockk()
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    @Test
    fun load_withNullId_setsNewState() = runTest(dispatcher) {
        val vm = EditJugadorViewModel(upsertJugador, getJugador, deleteJugador, validator)

        vm.onEvent(EditJugadorEvent.Load(null))
        runCurrent()

        val s = vm.state.value
        assertThat(s.jugadorId).isNull()
        assertThat(s.nombre).isEqualTo("")
        assertThat(s.partidas).isEqualTo("")
        assertThat(s.canBeDeleted).isFalse()
    }

    @Test
    fun load_withId_populatesFields() = runTest(dispatcher) {
        coEvery { getJugador(5) } returns Jugador(5, "Juan", 10)
        val vm = EditJugadorViewModel(upsertJugador, getJugador, deleteJugador, validator)

        vm.onEvent(EditJugadorEvent.Load(5))
        runCurrent()

        val s = vm.state.value
        assertThat(s.jugadorId).isEqualTo(5)
        assertThat(s.nombre).isEqualTo("Juan")
        assertThat(s.partidas).isEqualTo("10")
        assertThat(s.canBeDeleted).isTrue()
    }

    @Test
    fun save_withInvalidInputs_setsErrorsAndDoesNotSave() = runTest(dispatcher) {
        coEvery { validator.validateNombre("") } returns edu.ucne.composeTarea1.domain.validation.ValidationResult(false, "Nombre requerido")
        coEvery { validator.validatePartida("abc") } returns edu.ucne.composeTarea1.domain.validation.ValidationResult(false, "Partidas inválidas")
        coEvery { validator.validateNombreUnico(any(), any()) } returns edu.ucne.composeTarea1.domain.validation.ValidationResult(true)

        val vm = EditJugadorViewModel(upsertJugador, getJugador, deleteJugador, validator)

        vm.onEvent(EditJugadorEvent.NombreChanged(""))
        vm.onEvent(EditJugadorEvent.PartidasChanged("abc"))
        vm.onEvent(EditJugadorEvent.Save)
        runCurrent()

        val s = vm.state.value
        assertThat(s.nombreError).isEqualTo("Nombre requerido")
        assertThat(s.partidasError).isEqualTo("Partidas inválidas")
        assertThat(s.isSaved).isFalse()
    }

    @Test
    fun save_withValidInputs_callsUpsert_andSetsSavedTrue() = runTest(dispatcher) {
        coEvery { validator.validateNombre("Pedro") } returns edu.ucne.composeTarea1.domain.validation.ValidationResult(true)
        coEvery { validator.validatePartida("7") } returns edu.ucne.composeTarea1.domain.validation.ValidationResult(true)
        coEvery { validator.validateNombreUnico("Pedro", null) } returns edu.ucne.composeTarea1.domain.validation.ValidationResult(true)
        coEvery { upsertJugador(any()) } returns Result.success(99)

        val vm = EditJugadorViewModel(upsertJugador, getJugador, deleteJugador, validator)

        vm.onEvent(EditJugadorEvent.NombreChanged("Pedro"))
        vm.onEvent(EditJugadorEvent.PartidasChanged("7"))
        vm.onEvent(EditJugadorEvent.Save)
        runCurrent()

        val s = vm.state.value
        assertThat(s.isSaving).isFalse()
        assertThat(s.isSaved).isTrue()
        assertThat(s.jugadorId).isEqualTo(99)
    }

    @Test
    fun delete_whenHasId_callsUseCase_andFlagsDeleted() = runTest(dispatcher) {
        coEvery { deleteJugador(8) } returns Unit
        val vm = EditJugadorViewModel(upsertJugador, getJugador, deleteJugador, validator)
        coEvery { getJugador(8) } returns Jugador(8, "Ana", 3)
        vm.onEvent(EditJugadorEvent.Load(8))
        runCurrent()

        vm.onEvent(EditJugadorEvent.Delete)
        runCurrent()

        coVerify { deleteJugador(8) }
        val s = vm.state.value
        assertThat(s.isDeleting).isFalse()
        assertThat(s.isSaved).isTrue()
        assertThat(s.jugadorId).isNull()
        assertThat(s.nombre).isEqualTo("")
        assertThat(s.partidas).isEqualTo("")
        assertThat(s.canBeDeleted).isFalse()
    }
}