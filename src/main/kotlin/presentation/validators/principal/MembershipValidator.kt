package top.dedicado.presentation.validators.principal

import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import top.dedicado.presentation.dto.principal.membership.MembershipCreate

class MembershipValidator {
    fun configure(config: RequestValidationConfig) {
        config.apply {
            validate<MembershipCreate> { request -> create(request) }
        }
    }

    private fun create(request: MembershipCreate): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.accountId.isBlank()) {
            reasons.add("O ID da conta não pode ser vazio.")
        }

        if (request.projectId.isBlank()) {
            reasons.add("O ID do projeto não pode ser vazio.")
        }

        if (request.attribute == null) {
            reasons.add("O atributo não pode ser vazio.")
        }

        if (request.realm.name.isBlank()) {
            reasons.add("O realm não pode ser vazio.")
        }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }

}