package edu.ucne.composeTarea1.domain.validation

import edu.ucne.composeTarea1.domain.repository.LogroRepository
import javax.inject.Inject

class LogroValidator @Inject constructor(
    private val repository: LogroRepository
){
    fun validateNombre(nombre: String): ValidationResult {
        if (nombre.isBlank()) {
            return ValidationResult(
                isValid = false,
                error = "El nombre no puede estar vacío"
            )
        }
        if (nombre.length < 3) {
            return ValidationResult(
                isValid = false,
                error = "El nombre debe tener al menos 3 caracteres"
            )
        }
        if (nombre.length > 50) {
            return ValidationResult(
                isValid = false,
                error = "El nombre no puede tener más de 50 caracteres"
            )
        }
        return ValidationResult(isValid = true)
    }

    fun validateDescripcion(descripcion: String): ValidationResult {
        if(descripcion.isBlank()){
            return ValidationResult(
                isValid = false,
                error = "La descripción no puede estar vacía"
            )
        }
        if(descripcion.length < 10){
            return ValidationResult(
                isValid = false,
                error = "La descripción debe tener al menos 10 caracteres"
            )
        }
        if(descripcion.length > 200){
            return ValidationResult(
                isValid = false,
                error = "La descripción no puede tener más de 200 caracteres"
            )
        }
        return ValidationResult(isValid = true)
    }

    suspend fun validateNombreUnico(nombre: String, currentLogroId: Int? = null): ValidationResult {
        val basicValidation = validateNombre(nombre)
        if (!basicValidation.isValid) {
            return basicValidation
        }
        val existingLogro = repository.getByNombre(nombre)

        if(existingLogro != null) {
            if(currentLogroId != null && existingLogro.logroId == currentLogroId) {
                return ValidationResult(isValid = true)
            }else{
                return ValidationResult(
                    isValid = false,
                    error = "Ya existe un logro con este nombre"
                )
            }
        }
        return ValidationResult(isValid = true)
    }
}