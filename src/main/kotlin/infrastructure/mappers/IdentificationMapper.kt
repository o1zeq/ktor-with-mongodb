package top.dedicado.infrastructure.mappers

import org.bson.Document
import top.dedicado.domain.models.Identification
import top.dedicado.utils.extractors.DocumentInstantExtractor

object IdentificationMapper {

    fun toDocument(fields: Identification): Document {
        return Document().apply {
            put(Identification::kind.name, fields.kind)
            put(Identification::code.name, fields.code)
            fields.issuer?.let { put(Identification::issuer.name, it) }
            fields.placeOfIssue?.let { put(Identification::placeOfIssue.name, it) }
            fields.issuedOn?.let { put(Identification::issuedOn.name, it) }
            fields.validUntil?.let { put(Identification::validUntil.name, it) }
            fields.photo?.let { put(Identification::photo.name, it) }
            fields.fingerprint?.let { put(Identification::fingerprint.name, it) }
        }
    }

    fun toMap(fields: Identification): Map<String, Any?> {
        val update = mutableMapOf<String, Any?>()

        update[Identification::kind.name] = fields.kind
        update[Identification::code.name] = fields.code
        fields.issuer?.let { update[Identification::issuer.name] = it }
        fields.placeOfIssue?.let { update[Identification::placeOfIssue.name] = it }
        fields.issuedOn?.let { update[Identification::issuer.name] = it }
        fields.validUntil?.let { update[Identification::validUntil.name] = it }
        fields.photo?.let { update[Identification::photo.name] = it }
        fields.fingerprint?.let { update[Identification::fingerprint.name] = it }

        return update.filterValues { it != null }
    }

    fun toModel(document: Document): Identification {
        return Identification(
            kind = document.getString(Identification::kind.name),
            code = document.getString(Identification::code.name),
            issuer = document.getString(Identification::issuer.name),
            placeOfIssue = document.getString(Identification::placeOfIssue.name),
            issuedOn = DocumentInstantExtractor.extractInstantNullable(document, Identification::issuedOn.name),
            validUntil = DocumentInstantExtractor.extractInstantNullable(document, Identification::validUntil.name),
            photo = document.getString(Identification::photo.name),
            fingerprint = document.getString(Identification::fingerprint.name),
        )
    }
}