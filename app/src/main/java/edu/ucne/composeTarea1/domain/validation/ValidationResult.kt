package edu.ucne.composeTarea1.domain.validation

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)
