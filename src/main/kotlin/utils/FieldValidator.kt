package top.dedicado.utils

import top.dedicado.domain.models.Coordinate
import top.dedicado.domain.models.Position
import top.dedicado.domain.models.Address
import top.dedicado.domain.models.Identification
import top.dedicado.domain.models.Signature

object FieldValidator {

    fun isValidEmail(email: String): Boolean {
        return "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex().matches(email)
    }

    fun isValidPhone(phone: String): Boolean {
        return "^\\+\\d{13}\$".toRegex().matches(phone)
    }

    fun isValidIdentity(identity: String): Boolean {
        return "^\\d{11,14}\$".toRegex().matches(identity)
    }

    fun isValidPlate(plate: String): Boolean {
        return "^[A-Za-z]{3}\\d{4}$".toRegex().matches(plate)
    }

    fun validateAddress(address: Address): List<String> {
        val reasons = mutableListOf<String>()

        if (address.kind?.isBlank() == true) {
            reasons.add("O tipo de endereço não pode ser vazio.")
        }

        address.zipcode?.let {
            if (it.length != 8) {
                reasons.add("O CEP deve ter exatamente 8 dígitos.")
            }
            if (it.isBlank()) {
                reasons.add("O CEP não pode ser vazio se fornecido.")
            }
        }

        if (address.zipcode?.isBlank() == true) {
            reasons.add("O CEP não pode ser vazio se fornecido.")
        }

        if (address.street?.isBlank() == true) {
            reasons.add("O logradouro não pode ser vazia se fornecida.")
        }

        if (address.city?.isBlank() == true) {
            reasons.add("A cidade não pode ser vazia se fornecida.")
        }

        if (address.state?.isBlank() == true) {
            reasons.add("O estado não pode ser vazio se fornecido.")
        }

        if (address.country?.isBlank() == true) {
            reasons.add("O país não pode ser vazio se fornecido.")
        }

        address.coordinate?.let { reasons.addAll(validateCoordinate(it)) }

        return reasons
    }

    fun validateCoordinate(coordinate: Coordinate): List<String> {
        val reasons = mutableListOf<String>()
        coordinate.latitude.let {
            if (it < -90.0 || it > 90.0) {
                reasons.add("A latitude em 'position' deve estar entre -90 e 90.")
            }
        }
        coordinate.longitude.let {
            if (it < -180.0 || it > 180.0) {
                reasons.add("A longitude em 'position' deve estar entre -180 e 180.")
            }
        }
        return reasons
    }

    fun validateIdentification(identification: Identification): List<String> {
        val reasons = mutableListOf<String>()

        if (identification.kind.isBlank()) {
            reasons.add("O tipo de identificação não pode ser vazio.")
        }
        if (identification.code.isBlank()) {
            reasons.add("O código não pode ser vazio.")
        }
        if (identification.issuer.isNullOrBlank()) {
            reasons.add("O emissor não pode ser vazio.")
        }
        if (identification.placeOfIssue.isNullOrBlank()) {
            reasons.add("O local de emissão não pode ser vazio.")
        }
        if (identification.issuedOn != null && identification.validUntil?.let { identification.issuedOn <= it } == true) {
            reasons.add("A data de emissão não pode ser posterior à data de validade.")
        }
        if (identification.photo.isNullOrBlank()) {
            reasons.add("A foto não pode ser vazia.")
        }
        if (identification.fingerprint.isNullOrBlank()) {
            reasons.add("A impressão digital não pode ser vazia.")
        }

        return reasons
    }

    fun validatePosition(position: Position): List<String> {
        val reasons = mutableListOf<String>()

        position.accuracy?.let { accuracy ->
            if (accuracy < 0.0f) {
                reasons.add("A precisão (accuracy) deve ser um valor positivo.")
            }
        }

        position.bearing?.let { bearing ->
            if (bearing !in 0.0f..360.0f) {
                reasons.add("O azimute (bearing) deve estar entre 0 e 360 graus.")
            }
        }

        position.speed?.let { speed ->
            if (speed < 0.0f) {
                reasons.add("A velocidade (speed) não pode ser negativa.")
            }
        }

        position.altitude?.let { altitude ->
            if (altitude < -500.0 || altitude > 10000.0) {
                reasons.add("A altitude deve estar entre -500 e 10.000 metros.")
            }
        }

        reasons.addAll(validateCoordinate(position.coordinate))

        return reasons
    }

    fun validateSignature(signature: Signature): List<String> {
        val reasons = mutableListOf<String>()

        if (signature.name.isBlank()) {
            reasons.add("O nome não pode ser vazio.")
        }
        if (signature.email.isBlank() || !isValidEmail(signature.email)) {
            reasons.add("O e-mail fornecido é inválido.")
        }
        if (signature.phone != null && !isValidPhone(signature.phone)) {
            reasons.add("O telefone fornecido é inválido. Deve ter 14 dígitos e começar com '+'. Ex: +5511987654321")
        }
        if (signature.photo.isNullOrBlank()) {
            reasons.add("A foto não pode ser vazia.")
        }

        return reasons
    }
}