package top.dedicado.presentation.validators.principal

import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import top.dedicado.presentation.dto.principal.project.ProjectCreate
import top.dedicado.presentation.dto.principal.project.ProjectUpdate
import top.dedicado.utils.FieldValidator

class ProjectValidator {
    fun configure(config: RequestValidationConfig) {
        config.apply {
            validate<ProjectCreate> { request -> create(request) }
            validate<ProjectUpdate> { request -> update(request) }
        }
    }

    private fun create(request: ProjectCreate): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.name?.isBlank() == true ) {
            reasons.add("O nome não pode ser vazio.")
        }

        if (request.identifier?.isBlank() == true) {
            reasons.add("O nome de identificação (identifier) não pode ser vazio.")
        } else request.identifier?.length?.let {
            if (it < 4) {
                reasons.add("O nome de identificação (identifier) deve ter pelo menos 4 caracteres.")
            }
        }

        if (request.slogan?.isBlank() == true ) {
            reasons.add("O slogan não pode ser vazio.")
        }

        if (request.description?.isBlank() == true) {
            reasons.add("A descrição não pode ser vazia.")
        }

        if (request.avatar.isNullOrBlank()) {
            reasons.add("A foto não pode ser vazia.")
        }

        if (request.budget == null || request.budget < 0.00) {
            reasons.add("O orçamento não pode ser vazio e deve ser maior que zero.")
        }

        request.address?.let { reasons.addAll(FieldValidator.validateAddress(it)) }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }

    private fun update(request: ProjectUpdate): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.name?.isBlank() == true) {
            reasons.add("O nome não pode ser vazio.")
        }

        if (request.identifier?.isBlank() == true) {
            reasons.add("O nome de identificação (identifier) não pode ser vazio.")
        } else request.identifier?.length?.let {
            if (it < 4) {
                reasons.add("O nome de identificação (identifier) deve ter pelo menos 4 caracteres.")
            }
        }

        if (request.slogan?.isBlank() == true ) {
            reasons.add("O slogan não pode ser vazio.")
        }

        if (request.description?.isBlank() == true) {
            reasons.add("A descrição não pode ser vazia.")
        }

        if (request.avatar?.isBlank() == true) {
            reasons.add("A foto não pode ser vazia.")
        }

        if (request.budget == null || request.budget < 0.00) {
            reasons.add("O orçamento não pode ser vazio e deve ser maior que zero.")
        }

        request.address?.let { reasons.addAll(FieldValidator.validateAddress(it)) }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }

}