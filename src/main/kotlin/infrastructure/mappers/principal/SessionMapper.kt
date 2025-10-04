package top.dedicado.infrastructure.mappers.principal

import kotlinx.datetime.toJavaInstant
import org.bson.Document
import org.bson.types.ObjectId
import top.dedicado.domain.models.Realm
import top.dedicado.domain.models.principal.Session
import top.dedicado.presentation.dto.principal.session.SessionCreate
import top.dedicado.presentation.dto.principal.session.SessionUpdate
import top.dedicado.utils.extractors.DocumentInstantExtractor

object SessionMapper {

    fun toDocument(fields: SessionCreate): Document {
        return Document().apply {
            fields.id?.let { put("_id", ObjectId(it)) }
            fields.timestamp?.let { put(fields::timestamp.name, it.toJavaInstant()) }
            fields.expiresIn?.let { put(fields::expiresIn.name, it.toJavaInstant()) }
            put(fields::refreshIdToken.name, fields.refreshIdToken)
            put(fields::issuer.name, fields.issuer)
            put(fields::accountId.name, ObjectId(fields.accountId))
            put(fields::realm.name, fields.realm)
        }
    }

    fun toMap(fields: SessionUpdate): Map<String, Any?> {
        val update = mutableMapOf<String, Any?>()

        fields.expiresIn?.let{ update[fields::expiresIn.name] =  it.toJavaInstant() }
        update[fields::refreshIdToken.name] = fields.refreshIdToken
        update[fields::issuer.name] = fields.issuer

        return update.filterValues { it != null }
    }

    fun toModel(document: Document): Session {
        return Session(
            id = document.getObjectId("_id").toString(),
            timestamp = DocumentInstantExtractor.extractInstant(document, Session::timestamp.name),
            expiresIn = DocumentInstantExtractor.extractInstant(document, Session::expiresIn.name),
            refreshIdToken = document.getString(Session::refreshIdToken.name),
            issuer = document.getString(Session::issuer.name),
            accountId = document.getObjectId(Session::accountId.name).toString(),
            realm = document[Session::realm.name].let {
                Realm.valueOf(it.toString())
            }
        )
    }
}