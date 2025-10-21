package edu.ucne.composeTarea1.domain.usecase.partidaUseCase

import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPartidaEnProgresoUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    operator fun invoke(): Flow<Partida?> = repository.getPartidaEnProgreso()
}