package edu.ucne.composeTarea1.domain.usecase.movimientosUseCase

import edu.ucne.composeTarea1.data.remote.repository.MovimientosRepository
import edu.ucne.composeTarea1.domain.model.Movimientos
import javax.inject.Inject

class postMovimientosUseCase @Inject constructor(
    private val repository: MovimientosRepository
) {
    suspend operator fun invoke(partidaId: Int, movimiento: Movimientos) =
        repository.postMovimiento(partidaId, movimiento)
}