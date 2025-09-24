package edu.ucne.composeTarea1.domain.usecase.LogroUseCase

import edu.ucne.composeTarea1.domain.repository.LogroRepository
import javax.inject.Inject

class ObserveLogroUseCase @Inject constructor(
    private val repository: LogroRepository
) {
    suspend operator fun invoke() = repository.observeLogro()
}