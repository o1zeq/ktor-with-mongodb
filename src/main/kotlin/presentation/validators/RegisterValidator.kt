package top.dedicado.presentation.validators

import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import top.dedicado.presentation.dto.register.RegisterRequest
import top.dedicado.utils.FieldValidator

class RegisterValidator {
    fun configure(config: RequestValidationConfig) {
        config.apply {
            validate<RegisterRequest> { request -> insert(request) }
        }
    }

    private fun insert(request: RegisterRequest): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.realm.name.isBlank()) {
            reasons.add("O realm não pode ser vazio.")
        }

        if (request.identifier.isBlank()) {
            reasons.add("O nome de identificação (identifier) não pode ser vazio.")
        } else if (request.identifier.length < 4) {
            reasons.add("O nome de identificação (identifier) deve ter pelo menos 4 caracteres.")
        }

        if (request.name.isBlank()) {
            reasons.add("O nome não pode ser vazio.")
        }

        if (request.email.isBlank() || !FieldValidator.isValidEmail(request.email)) {
            reasons.add("O e-mail fornecido é inválido.")
        }

        if (request.phone.isBlank() || !FieldValidator.isValidPhone(request.phone)) {
            reasons.add("O telefone fornecido é inválido. Deve ter 14 dígitos e começar com '+'. Ex: +5511987654321")
        }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }
}