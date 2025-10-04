package top.dedicado.presentation.validators.principal

import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import top.dedicado.presentation.dto.principal.session.SessionCreate
import top.dedicado.presentation.dto.principal.session.SessionUpdate

class SessionValidator {
    fun configure(config: RequestValidationConfig) {
        config.apply {
            validate<SessionCreate> { request -> create(request) }
            validate<SessionUpdate> { request -> update(request) }
        }
    }

    private fun create(request: SessionCreate): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.refreshIdToken.isBlank()) {
            reasons.add("O token de atualização não pode ser vazio.")
        }

        if (request.issuer.isBlank()) {
            reasons.add("O emissor não pode ser vazio.")
        }

        if (request.accountId.isBlank()) {
            reasons.add("O ID da associação não pode ser vazio.")
        }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }

    private fun update(request: SessionUpdate): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.refreshIdToken.isBlank()) {
            reasons.add("O token de atualização não pode ser vazio.")
        }

        if (request.issuer.isBlank()) {
            reasons.add("O emissor não pode ser vazio.")
        }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }
}