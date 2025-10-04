package top.dedicado.presentation.validators

import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import top.dedicado.presentation.dto.auth.AuthRequest

class AuthValidator {
    fun configure(config: RequestValidationConfig) {
        config.apply {
            validate<AuthRequest> { request -> login(request) }
        }
    }

    private fun login(request: AuthRequest): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.realm.name.isBlank()) {
            reasons.add("O realm não pode ser vazio.")
        }

        if (request.identifier.isBlank()) {
            reasons.add("O nome de identificação (identifier) não pode ser vazio.")
        } else if (request.identifier.length < 4) {
            reasons.add("O nome de identificação (identifier) deve ter pelo menos 4 caracteres.")
        }

        if (request.password.isBlank()) {
            reasons.add("A senha não pode ser vazia.")
        } else if (request.password.length < 8) {
            reasons.add("A senha deve ter pelo menos 8 caracteres.")
        }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }
}