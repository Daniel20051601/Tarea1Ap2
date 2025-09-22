package edu.ucne.composeTarea1.domain.usecase.jugadorUseCase

import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import javax.inject.Inject

class DeleteJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteJugador(id)
}