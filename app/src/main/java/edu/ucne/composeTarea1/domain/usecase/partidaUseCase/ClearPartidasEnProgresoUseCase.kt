package edu.ucne.composeTarea1.domain.usecase.partidaUseCase

import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import javax.inject.Inject

class ClearPartidasEnProgresoUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    suspend operator fun invoke() = repository.clearPartidasEnProgreso()
}