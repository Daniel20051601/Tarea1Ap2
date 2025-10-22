package edu.ucne.composeTarea1.domain.tareas.usecase.logro_usecase

import edu.ucne.composeTarea1.domain.repository.LogroRepository
import edu.ucne.composeTarea1.domain.usecase.LogroUseCase.DeleteLogroUseCase

import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteLogroUseCaseTest {
    private lateinit var repository: LogroRepository
    private lateinit var useCase: DeleteLogroUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = DeleteLogroUseCase(repository)
    }

    @Test
    fun `calls repository to delete logro by id`() = runTest {
        val logroId = 5
        coEvery { repository.deleteById(logroId) } returns Unit

        useCase(logroId)

        coVerify(exactly = 1) { repository.deleteById(logroId) }
    }
}
