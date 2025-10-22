package edu.ucne.composeTarea1.domain.usecase.LogroUseCase

import edu.ucne.composeTarea1.domain.model.Logro
import edu.ucne.composeTarea1.domain.repository.LogroRepository
import edu.ucne.composeTarea1.domain.validation.LogroValidator
import javax.inject.Inject

class UpsertLogroUseCase @Inject constructor(
    private val repository: LogroRepository,
    private val validator: LogroValidator
) {
    suspend operator fun invoke(logro: Logro): Result<Int> {
        val nombreResult = validator.validateNombre(logro.nombre)
        if(!nombreResult.isValid){
            return Result.failure(Exception(nombreResult.error))
        }
        val descripcionResult = validator.validateDescripcion(logro.descripcion)
        if(!descripcionResult.isValid){
            return Result.failure(Exception(descripcionResult.error))
        }
        return runCatching { repository.upsertLogro(logro) }
    }
}