package edu.ucne.composeTarea1.domain.usecase

import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.domain.validation.JugadorValidator
import javax.inject.Inject

class UpsertJugadorUseCase @Inject constructor(
    private val repository: JugadorRepository,
    private val validator: JugadorValidator
) {
    suspend operator fun invoke(jugador: Jugador): Result<Int>{
        val nombreResult =validator.validateNombre(jugador.nombres)
        if(!nombreResult.isValid){
            return Result.failure(IllegalArgumentException(nombreResult.error))
        }
        val partidaResult = validator.validatePartida(jugador.partidas.toString())
        if((!partidaResult.isValid)){
            return Result.failure(IllegalArgumentException(partidaResult.error))
        }
        return runCatching { repository.upsertJugador(jugador) }
    }
}