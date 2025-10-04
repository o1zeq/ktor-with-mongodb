package top.dedicado.infrastructure.mappers

import org.bson.Document
import top.dedicado.domain.models.Signature

object SignatureMapper {

    fun toDocument(fields: Signature): Document {
        return Document().apply {
            put(fields::name.name, fields.name)
            put(fields::email.name, fields.email)
            fields.phone?.let { put(fields::phone.name, it) }
            fields.photo?.let { put(fields::photo.name, it) }
        }
    }

    fun toMap(fields: Signature): Map<String, Any?> {
        val update = mutableMapOf<String, Any?>()

        update[fields::name.name] = fields.name
        update[fields::email.name] = fields.email
        fields.phone?.let { update[fields::phone.name] = it }
        fields.photo?.let { update[fields::photo.name] = it }

        return update.filterValues { it != null }
    }

    fun toModel(document: Document): Signature {
        return Signature(
            name = document.getString("name"),
            email = document.getString("email"),
            phone = document.getString("phone"),
            photo = document.getString("photo")
        )
    }
}