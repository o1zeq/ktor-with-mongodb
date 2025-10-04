package top.dedicado.presentation.validators.principal

import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import top.dedicado.presentation.dto.principal.contact.ContactCreate
import top.dedicado.presentation.dto.principal.contact.ContactUpdate
import top.dedicado.utils.FieldValidator

class ContactValidator {
    fun configure(config: RequestValidationConfig) {
        config.apply {
            validate<ContactCreate> { request -> create(request) }
            validate<ContactUpdate> { request -> update(request) }
        }
    }

    private fun create(request: ContactCreate): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.occupation?.isBlank() == true) {
            reasons.add("A profissão não pode ser vazia.")
        }

        if (request.projectId.isNullOrBlank()) {
            reasons.add("O ID do projeto não pode ser vazio.")
        }

        if (request.name?.isBlank() == true) {
            reasons.add("O nome não pode ser vazio.")
        }

        if (request.email?.isBlank() == true || request.email?.let { !FieldValidator.isValidEmail(it) } == true){
            reasons.add("O e-mail fornecido é inválido.")
        }

        if (request.phone?.isBlank() == true || request.phone?.let { !FieldValidator.isValidPhone(it) } == true) {
            reasons.add("O telefone fornecido é inválido. Deve ter 14 dígitos e começar com '+'. Ex: +5511987654321")
        }

        if (request.photo?.isBlank() == true) {
            reasons.add("A URL da foto não pode ser vazia.")
        }

        if (request.identifier?.isBlank() == true) {
            reasons.add("O nome de identificação (identifier) não pode ser vazio.")
        } else request.identifier?.length?.let {
            if (it < 4) {
                reasons.add("O nome de identificação (identifier) deve ter pelo menos 4 caracteres.")
            }
        }

        if (request.kind.isNullOrBlank()) {
            reasons.add("O tipo não pode ser vazio.")
        }

        if (request.note.isNullOrBlank()) {
            reasons.add("A nota não pode ser vazia.")
        }

        request.identifications?.let {
            it.forEach { identification -> reasons.addAll(FieldValidator.validateIdentification(identification)) }
        }

        request.address?.let { reasons.addAll(FieldValidator.validateAddress(it)) }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }

    private fun update(request: ContactUpdate): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.occupation?.isBlank() == true) {
            reasons.add("A profissão não pode ser vazia.")
        }

        if (request.name?.isBlank() == true) {
            reasons.add("O nome não pode ser vazio.")
        }

        if (request.email?.isBlank() == true || request.email?.let { !FieldValidator.isValidEmail(it) } == true){
            reasons.add("O e-mail fornecido é inválido.")
        }

        if (request.phone?.isBlank() == true || request.phone?.let { !FieldValidator.isValidPhone(it) } == true) {
            reasons.add("O telefone fornecido é inválido. Deve ter 14 dígitos e começar com '+'. Ex: +5511987654321")
        }

        if (request.photo?.isBlank() == true) {
            reasons.add("A URL da foto não pode ser vazia.")
        }

        if (request.identifier?.isBlank() == true) {
            reasons.add("O nome de identificação (identifier) não pode ser vazio.")
        } else request.identifier?.length?.let {
            if (it < 4) {
                reasons.add("O nome de identificação (identifier) deve ter pelo menos 4 caracteres.")
            }
        }

        if (request.kind.isNullOrBlank()) {
            reasons.add("O tipo não pode ser vazio.")
        }

        if (request.note.isNullOrBlank()) {
            reasons.add("A nota não pode ser vazia.")
        }

        request.identifications?.let {
            it.forEach { identification -> reasons.addAll(FieldValidator.validateIdentification(identification)) }
        }

        request.address?.let { reasons.addAll(FieldValidator.validateAddress(it)) }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }
}