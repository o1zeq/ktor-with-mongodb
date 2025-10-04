package top.dedicado.presentation.validators.principal

import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import kotlinx.datetime.Clock
import top.dedicado.presentation.dto.principal.subscription.SubscriptionCreate
import top.dedicado.presentation.dto.principal.subscription.SubscriptionUpdate

class SubscriptionValidator {
    fun configure(config: RequestValidationConfig) {
        config.apply {
            validate<SubscriptionCreate> { request -> create(request) }
            validate<SubscriptionUpdate> { request -> update(request) }
        }
    }

    private fun create(request: SubscriptionCreate): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.startsIn != null && request.startsIn < Clock.System.now()) {
            reasons.add("A data de início não pode ser anterior à data atual.")
        }

        if (request.expiresIn != null && request.expiresIn < Clock.System.now()) {
            reasons.add("A data de expiração não pode ser anterior à data atual.")
        }

        if (request.status.isNullOrBlank()) {
            reasons.add("O status não pode ser vazio.")
        }

        if (request.plan == null) {
            reasons.add("O plano não pode ser nulo.")
        }

        if (request.price != null && request.price < 0) {
            reasons.add("O preço não pode ser negativo.")
        }

        if (request.accountId.isBlank()) {
            reasons.add("O ID da conta não pode ser vazio.")
        }

        if (request.projectId.isBlank()) {
            reasons.add("O ID do projeto não pode ser vazio.")
        }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }

    private fun update(request: SubscriptionUpdate): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.startsIn != null && request.startsIn < Clock.System.now()) {
            reasons.add("A data de início não pode ser anterior à data atual.")
        }

        if (request.expiresIn != null && request.expiresIn < Clock.System.now()) {
            reasons.add("A data de expiração não pode ser anterior à data atual.")
        }

        if (request.status.isNullOrBlank()) {
            reasons.add("O status não pode ser vazio.")
        }

        if (request.plan != null) {
            reasons.add("O plano não pode ser alterado.")
        }

        if (request.price != null && request.price < 0) {
            reasons.add("O preço não pode ser negativo.")
        }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }
}