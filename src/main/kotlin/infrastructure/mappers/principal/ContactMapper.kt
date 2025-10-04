package top.dedicado.infrastructure.mappers.principal

import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.bson.Document
import org.bson.types.ObjectId
import top.dedicado.domain.models.principal.Contact
import top.dedicado.infrastructure.mappers.AddressMapper
import top.dedicado.infrastructure.mappers.IdentificationMapper
import top.dedicado.presentation.dto.principal.contact.ContactCreate
import top.dedicado.presentation.dto.principal.contact.ContactUpdate
import top.dedicado.utils.extractors.DocumentInstantExtractor

object ContactMapper {

    fun toDocument(fields: ContactCreate): Document {

        return Document().apply {
            fields.id?.let { put("_id", ObjectId(it)) }
            fields.createdAt?.let { put(fields::createdAt.name, it.toJavaInstant()) }
            fields.updatedAt?.let { put(fields::updatedAt.name, it.toJavaInstant()) }
            fields.deletedAt?.let { put(fields::deletedAt.name, it.toJavaInstant()) }
            fields.softDeleted?.let { put(fields::softDeleted.name, it) }
            put(fields::projectId.name, ObjectId(fields.projectId))
            fields.identifier?.let { put(fields::identifier.name, it) }
            fields.occupation?.let { put(fields::occupation.name, it) }
            fields.name?.let { put(fields::name.name, it) }
            fields.phone?.let { put(fields::phone.name, it) }
            fields.email?.let { put(fields::email.name, it) }
            fields.photo?.let { put(fields::photo.name, it) }
            fields.kind?.let { put(fields::kind.name, it) }
            fields.note?.let { put(fields::note.name, it) }
            fields.identifications?.let { put(fields::identifications.name, it.map {
                identification -> IdentificationMapper.toDocument(identification) })
            }
            fields.address?.let { put(fields::address.name, AddressMapper.toDocument(it)) }
        }
    }

    fun toMap(fields: ContactUpdate): Map<String, Any?> {
        val update = mutableMapOf<String, Any?>()

        update["updatedAt"] = Clock.System.now().toJavaInstant()
        fields.identifier?.let {update[fields::identifier.name] = it }
        fields.occupation?.let { update[fields::occupation.name] = it }
        fields.name?.let { update[fields::name.name] = it }
        fields.phone?.let { update[fields::phone.name] = it }
        fields.email?.let { update[fields::email.name] = it }
        fields.photo?.let { update[fields::photo.name] = it }
        fields.kind?.let { update[fields::kind.name] = it }
        fields.note?.let { update[fields::note.name] = it }
        fields.identifications?.let { update[fields::identifications.name] = it.map {
            identification -> IdentificationMapper.toMap(identification)
        } }
        fields.address?.let { update[fields::address.name] = AddressMapper.toMap(it) }

        return update.filterValues { it != null }
    }

    fun toModel(document: Document): Contact {
        return Contact(
            id = document.getObjectId("_id").toString(),
            createdAt = DocumentInstantExtractor.extractInstant(document, Contact::createdAt.name),
            updatedAt = DocumentInstantExtractor.extractInstant(document, Contact::updatedAt.name),
            deletedAt = DocumentInstantExtractor.extractInstantNullable(document, Contact::deletedAt.name),
            softDeleted = document.getBoolean(Contact::softDeleted.name),
            projectId = document.getObjectId(Contact::projectId.name).toString(),
            identifier = document.getString(Contact::identifier.name),
            occupation = document.getString(Contact::occupation.name),
            name = document.getString(Contact::name.name),
            phone = document.getString(Contact::phone.name),
            email = document.getString(Contact::email.name),
            photo = document.getString(Contact::photo.name),
            kind = document.getString(Contact::kind.name),
            note = document.getString(Contact::note.name),
            identifications = document[Contact::identifications.name]?.let { identifications ->
                identifications as List<*>
                identifications.map { identification ->
                    IdentificationMapper.toModel(identification as Document)
                }
            },
            address = document[Contact::address.name]?.let { address -> AddressMapper.toModel(address as Document)}
        )
    }
}