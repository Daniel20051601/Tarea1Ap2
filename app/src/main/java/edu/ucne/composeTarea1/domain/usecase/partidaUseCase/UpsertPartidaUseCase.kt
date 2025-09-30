package edu.ucne.composeTarea1.domain.usecase.partidaUseCase

import edu.ucne.composeTarea1.domain.model.Partida
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import javax.inject.Inject

class UpsertPartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    suspend operator fun invoke(partida: Partida) {
        repository.upsert(partida)
    }
}
