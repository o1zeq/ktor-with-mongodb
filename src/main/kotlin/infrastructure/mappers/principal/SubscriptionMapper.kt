package top.dedicado.infrastructure.mappers.principal

import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.bson.Document
import org.bson.types.ObjectId
import top.dedicado.domain.models.Plan
import top.dedicado.domain.models.principal.Subscription
import top.dedicado.presentation.dto.principal.subscription.SubscriptionCreate
import top.dedicado.presentation.dto.principal.subscription.SubscriptionUpdate
import top.dedicado.utils.extractors.DocumentInstantExtractor

object SubscriptionMapper {

    fun toDocument(fields: SubscriptionCreate): Document {
        return Document().apply {
            fields.id?.let { put("_id", ObjectId(it)) }
            fields.createdAt?.let { put(fields::createdAt.name, it.toJavaInstant()) }
            fields.updatedAt?.let { put(fields::updatedAt.name, it.toJavaInstant()) }
            fields.deletedAt?.let { put(fields::deletedAt.name, it.toJavaInstant()) }
            fields.softDeleted?.let { put(fields::softDeleted.name, it) }
            fields.startsIn?.let { put(fields::startsIn.name, it.toJavaInstant()) }
            fields.expiresIn?.let { put(fields::expiresIn.name, it.toJavaInstant()) }
            fields.status?.let { put(fields::status.name, it) }
            fields.plan?.let { put(fields::plan.name, it.name) }
            fields.price?.let { put(fields::price.name, it) }
            fields.code?.let { put(fields::code.name, it) }
            put(fields::accountId.name, ObjectId(fields.accountId))
            put(fields::projectId.name, ObjectId(fields.projectId))
        }
    }

    fun toMap(fields: SubscriptionUpdate): Map<String, Any?> {
        val update = mutableMapOf<String, Any?>()

        update["updatedAt"] = Clock.System.now().toJavaInstant()
        fields.startsIn?.let { update[fields::startsIn.name] = it.toJavaInstant()}
        fields.expiresIn?.let { update[fields::expiresIn.name] = it.toJavaInstant()}
        fields.status?.let { update[fields::status.name] = it }
        fields.plan?.let { update[fields::plan.name] = it.name }
        fields.price?.let { update[fields::price.name] = it }

        return update.filterValues { it != null }
    }

    fun toModel(document: Document): Subscription {
        return Subscription(
            id = document.getObjectId("_id").toString(),
            createdAt = DocumentInstantExtractor.extractInstant(document, Subscription::createdAt.name),
            updatedAt = DocumentInstantExtractor.extractInstant(document, Subscription::updatedAt.name),
            deletedAt = DocumentInstantExtractor.extractInstantNullable(document, Subscription::deletedAt.name),
            softDeleted = document.getBoolean(Subscription::softDeleted.name),
            startsIn = DocumentInstantExtractor.extractInstant(document, Subscription::startsIn.name),
            expiresIn = DocumentInstantExtractor.extractInstant(document, Subscription::expiresIn.name),
            status = document.getString(Subscription::status.name),
            plan = document[Subscription::plan.name].let {
                Plan.valueOf(it.toString())
            },
            price = document.getDouble(Subscription::price.name),
            code = document.getString(Subscription::code.name),
            accountId = document.getObjectId(Subscription::accountId.name).toString(),
            projectId = document.getObjectId(Subscription::projectId.name).toString(),
        )
    }
}