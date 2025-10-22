package edu.ucne.composeTarea1.domain.usecase.movimientosUseCase

import edu.ucne.composeTarea1.data.remote.repository.MovimientosRepository
import javax.inject.Inject

class getMovimientosUseCase @Inject constructor(
    private val repository: MovimientosRepository
) {
    suspend operator fun invoke(partidaId: Int) = repository.getMovimientos(partidaId)
}