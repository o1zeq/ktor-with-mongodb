package top.dedicado.infrastructure.mappers.principal

import kotlinx.datetime.toJavaInstant
import org.bson.Document
import org.bson.types.ObjectId
import top.dedicado.domain.models.Attribute
import top.dedicado.domain.models.Realm
import top.dedicado.domain.models.principal.Membership
import top.dedicado.presentation.dto.principal.membership.MembershipCreate
import top.dedicado.utils.extractors.DocumentInstantExtractor

object MembershipMapper {

    fun toDocument(fields: MembershipCreate): Document {

        return Document().apply {
            fields.id?.let { put("_id", ObjectId(it)) }
            fields.createdAt?.let { put(fields::createdAt.name, it.toJavaInstant()) }
            fields.updatedAt?.let { put(fields::updatedAt.name, it.toJavaInstant()) }
            fields.deletedAt?.let { put(fields::deletedAt.name, it.toJavaInstant()) }
            fields.softDeleted?.let { put(fields::softDeleted.name, it) }
            fields.available?.let { put(fields::available.name, it) }
            fields.attribute?.let { put(fields::attribute.name, it) }
            put(fields::realm.name, fields.realm.name)
            put(fields::accountId.name, ObjectId(fields.accountId))
            put(fields::projectId.name, ObjectId(fields.projectId))
        }
    }

    fun toModel(document: Document): Membership {
        return Membership(
            id = document.getObjectId("_id").toString(),
            createdAt = DocumentInstantExtractor.extractInstant(document, Membership::createdAt.name),
            updatedAt = DocumentInstantExtractor.extractInstant(document, Membership::updatedAt.name),
            deletedAt = DocumentInstantExtractor.extractInstantNullable(document, Membership::deletedAt.name),
            softDeleted = document.getBoolean(Membership::softDeleted.name),
            available = document.getBoolean(Membership::available.name),
            attribute = document[Membership::attribute.name].let {
                Attribute.valueOf(it.toString())
            },
            realm = document[Membership::realm.name].let {
                Realm.valueOf(it.toString())
            },
            accountId = document.getObjectId(Membership::accountId.name).toString(),
            projectId = document.getObjectId(Membership::projectId.name).toString(),
        )
    }
}