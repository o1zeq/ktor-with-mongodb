package top.dedicado.presentation.validators.component

import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import top.dedicado.presentation.dto.component.activity.ActivityCreate
import top.dedicado.utils.FieldValidator

class ActivityValidator {
    fun configure(config: RequestValidationConfig) {
        config.apply {
            validate<ActivityCreate> { request -> insert(request) }
        }
    }

    private fun insert(request: ActivityCreate): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.timestamp == null) {
            reasons.add("O timestamp não pode ser nulo.")
        }

        if (request.referenceId.isNullOrBlank()) {
            reasons.add("O ID de referência não pode ser vazio.")
        }

        if (request.context.isNullOrBlank()) {
            reasons.add("O contexto não pode ser vazio.")
        }

        if (request.action.isNullOrBlank()) {
            reasons.add("A ação não pode ser vazia.")
        }

        if (request.evidence.isNullOrBlank()) {
            reasons.add("A evidência não pode ser vazia.")
        }

        request.position?.let {
            reasons.addAll(FieldValidator.validatePosition(it))
        }

        request.signature?.let {
            reasons.addAll(FieldValidator.validateSignature(it))
        }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }
}