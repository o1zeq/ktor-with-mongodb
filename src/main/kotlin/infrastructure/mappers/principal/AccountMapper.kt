package top.dedicado.infrastructure.mappers.principal

import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.bson.Document
import org.bson.types.ObjectId
import top.dedicado.domain.models.Role
import top.dedicado.domain.models.principal.Account
import top.dedicado.infrastructure.mappers.AddressMapper
import top.dedicado.presentation.dto.principal.account.AccountCreate
import top.dedicado.presentation.dto.principal.account.AccountUpdate
import top.dedicado.utils.extractors.DocumentInstantExtractor

object AccountMapper {

    fun toDocument(fields: AccountCreate): Document {

        return Document().apply {
            fields.id?.let { put("_id", ObjectId(it)) }
            fields.createdAt?.let { put(fields::createdAt.name, it.toJavaInstant()) }
            fields.updatedAt?.let { put(fields::updatedAt.name, it.toJavaInstant()) }
            fields.deletedAt?.let { put(fields::deletedAt.name, it.toJavaInstant()) }
            fields.softDeleted?.let { put(fields::softDeleted.name, it) }
            fields.available?.let { put(fields::available.name, it) }
            fields.identity?.let { put(fields::identity.name, it) }
            fields.role?.let { put(fields::role.name, it.name) }
            put(fields::identifier.name, fields.identifier)
            put(fields::name.name, fields.name)
            put(fields::email.name, fields.email)
            put(fields::phone.name, fields.phone)
            fields.photo?.let { put(fields::photo.name, it) }
            fields.passHash?.let { put(fields::passHash.name, it) }
            fields.codeHash?.let { put(fields::codeHash.name, it) }
            fields.address?.let { put(fields::address.name, AddressMapper.toDocument(it)) }
        }
    }

    fun toMap(fields: AccountUpdate): Map<String, Any?> {
        val update = mutableMapOf<String, Any?>()

        update["updatedAt"] = Clock.System.now().toJavaInstant()
        fields.identity?.let { update[fields::identity.name] = it}
        update[fields::identifier.name] = fields.identifier
        update[fields::name.name] = fields.name
        update[fields::email.name] = fields.email
        update[fields::phone.name] = fields.phone
        update[fields::photo.name] = fields.photo
        fields.address?.let { update[fields::address.name] = AddressMapper.toMap(it) }

        return update.filterValues { it != null }
    }

    fun toModel(document: Document): Account {
        return Account(
            id = document.getObjectId("_id").toString(),
            createdAt = DocumentInstantExtractor.extractInstant(document, Account::createdAt.name),
            updatedAt = DocumentInstantExtractor.extractInstant(document, Account::updatedAt.name),
            deletedAt = DocumentInstantExtractor.extractInstantNullable(document, Account::deletedAt.name),
            softDeleted = document.getBoolean(Account::softDeleted.name),
            available = document.getBoolean(Account::available.name),
            identity = document.getString(Account::identity.name),
            identifier = document.getString(Account::identifier.name),
            role = document[Account::role.name].let {
                Role.valueOf(it.toString())
            },
            name = document.getString(Account::name.name),
            email = document.getString(Account::email.name),
            phone = document.getString(Account::phone.name),
            photo = document.getString(Account::photo.name),
            passHash = document.getString(Account::passHash.name),
            codeHash = document.getString(Account::codeHash.name),
            address = document[Account::address.name]?.let { address -> AddressMapper.toModel(address as Document)}
        )
    }
}