package edu.ucne.composeTarea1.domain.usecase.LogroUseCase

import edu.ucne.composeTarea1.domain.model.Logro
import edu.ucne.composeTarea1.domain.repository.LogroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLogroUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    suspend operator fun invoke(id: Int): Logro? = repository.getById(id)
}