package top.dedicado.infrastructure.mappers.component

import kotlinx.datetime.toJavaInstant
import org.bson.Document
import org.bson.types.ObjectId
import top.dedicado.domain.models.component.Activity
import top.dedicado.infrastructure.mappers.PositionMapper
import top.dedicado.infrastructure.mappers.SignatureMapper
import top.dedicado.presentation.dto.component.activity.ActivityCreate
import top.dedicado.utils.extractors.DocumentInstantExtractor

object ActivityMapper {

    fun toDocument(fields: ActivityCreate): Document {
        return Document().apply {
            fields.id?.let { put("_id", ObjectId(it)) }
            fields.timestamp?.let { put(fields::timestamp.name, it.toJavaInstant()) }
            fields.referenceId?.let { put(fields::referenceId.name, ObjectId(it)) }
            fields.context?.let { put(fields::context.name, it) }
            fields.action?.let { put(fields::action.name, it) }
            fields.evidence?.let { put(fields::evidence.name, it) }
            fields.position?.let { put(fields::position.name, PositionMapper.toDocument(it)) }
            fields.signature?.let { put(fields::signature.name, SignatureMapper.toDocument(it)) }
        }
    }

    fun toMap(fields: Activity): Map<String, Any?> {
        val update = mutableMapOf<String, Any?>()

        update[fields::timestamp.name] = fields.timestamp.toJavaInstant()
        fields.context?.let { update[fields::context.name] = it }
        fields.action?.let { update[fields::action.name] = it }
        fields.evidence?.let { update[fields::evidence.name] = it }
        fields.position?.let { update[fields::position.name] = PositionMapper.toMap(it) }
        fields.signature?.let { update[fields::signature.name] = SignatureMapper.toMap(it) }

        return update.filterValues { it != null }
    }

    fun toModel(document: Document): Activity {
        return Activity(
            id = document.getObjectId("id").toString(),
            timestamp = DocumentInstantExtractor.extractInstant(document, Activity::timestamp.name),
            referenceId = document.getObjectId(Activity::referenceId.name).toString(),
            context = document.getString(Activity::context.name),
            action = document.getString(Activity::action.name),
            evidence = document.getString(Activity::evidence.name),
            position = document[Activity::position.name]?.let { PositionMapper.toModel(it as Document)},
            signature = document[Activity::signature.name]?.let { SignatureMapper.toModel(it as Document) }
        )
    }
}