package top.dedicado.presentation.validators.principal

import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import top.dedicado.presentation.dto.ResetPassword
import top.dedicado.presentation.dto.UpdatePassword
import top.dedicado.presentation.dto.principal.account.AccountCreate
import top.dedicado.presentation.dto.principal.account.AccountUpdate
import top.dedicado.utils.FieldValidator

class AccountValidator {
    fun configure(config: RequestValidationConfig) {
        config.apply {
            validate<AccountCreate> { request -> create(request) }
            validate<AccountUpdate> { request -> update(request) }
            validate<UpdatePassword> { request -> updatePassword(request) }
            validate<ResetPassword> { request -> resetPassword(request) }
        }
    }

    private fun create(request: AccountCreate): ValidationResult {
        val reasons = mutableListOf<String>()

        if(request.identity?.isBlank() == true || request.identity?.let { !FieldValidator.isValidIdentity(it) } == true) {
            reasons.add("A identidade fornecida é inválida. Deve ter entre 11 (CPF) e 14 (CNPJ) dígitos.")
        }

        if (request.identifier.isBlank()) {
            reasons.add("O nome de identificação (identifier) não pode ser vazio.")
        } else if (request.identifier.length < 4) {
            reasons.add("O nome de identificação (identifier) deve ter pelo menos 4 caracteres.")
        }

        if (request.role == null) {
            reasons.add("O papel não pode ser vazio.")
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

        if (request.photo.isNullOrBlank()) {
            reasons.add("A foto não pode ser vazia.")
        }

        request.address?.let { reasons.addAll(FieldValidator.validateAddress(it)) }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }

    private fun update(request: AccountUpdate): ValidationResult {
        val reasons = mutableListOf<String>()

        if(request.identity?.isBlank() == true || request.identity?.let { !FieldValidator.isValidIdentity(it) } == true) {
            reasons.add("A identidade fornecida é inválida. Deve ter entre 11 (CPF) e 14 (CNPJ) dígitos.")
        }

        request.identifier?.let {
            if (it.isBlank()) {
                reasons.add("O nome de identificação (identifier) não pode ser vazio.")
            } else if (it.length < 4) {
                reasons.add("O nome de identificação (identifier) deve ter pelo menos 4 caracteres.")
            }
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
            reasons.add("A foto não pode ser vazia.")
        }

        request.address?.let { reasons.addAll(FieldValidator.validateAddress(it)) }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }

    private fun updatePassword(request: UpdatePassword): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.oldPassword.isBlank()) {
            reasons.add("A senha antiga não pode ser vazia.")
        } else if (request.oldPassword.length < 8) {
            reasons.add("A senha antiga deve ter pelo menos 8 caracteres.")
        }
        if (request.newPassword.isBlank()) {
            reasons.add("A nova senha não pode ser vazia.")
        }else if (request.newPassword.length < 8) {
            reasons.add("A nova senha deve ter pelo menos 8 caracteres.")
        }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }

    private fun resetPassword(request: ResetPassword): ValidationResult {
        val reasons = mutableListOf<String>()

        if (request.identifier.isBlank()) {
            reasons.add("O nome de identificação (identifier) não pode ser vazio.")
        } else if (request.identifier.length < 4) {
            reasons.add("O nome de identificação (identifier) deve ter pelo menos 4 caracteres.")
        }
        if (request.email.isBlank() || !FieldValidator.isValidEmail(request.email)) {
            reasons.add("O e-mail fornecido é inválido.")
        }

        return if (reasons.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(reasons)
    }
}