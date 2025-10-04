package top.dedicado.infrastructure.mappers.principal

import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.bson.Document
import org.bson.types.ObjectId
import top.dedicado.domain.models.principal.Project
import top.dedicado.infrastructure.mappers.AddressMapper
import top.dedicado.presentation.dto.principal.project.ProjectCreate
import top.dedicado.presentation.dto.principal.project.ProjectUpdate
import top.dedicado.utils.extractors.DocumentDoubleExtractor
import top.dedicado.utils.extractors.DocumentInstantExtractor

object ProjectMapper {

    fun toDocument(fields: ProjectCreate): Document {

        return Document().apply {
            fields.id?.let { put("_id", ObjectId(it)) }
            fields.createdAt?.let { put(fields::createdAt.name, it.toJavaInstant()) }
            fields.updatedAt?.let { put(fields::updatedAt.name, it.toJavaInstant()) }
            fields.deletedAt?.let { put(fields::deletedAt.name, it.toJavaInstant()) }
            fields.softDeleted?.let { put(fields::softDeleted.name, it) }
            fields.available?.let { put(fields::available.name, it) }
            fields.name?.let { put(fields::name.name, it) }
            fields.slogan?.let { put(fields::slogan.name, it) }
            fields.identifier?.let { put(fields::identifier.name, it) }
            fields.description?.let { put(fields::description.name, it) }
            fields.avatar?.let { put(fields::avatar.name, it) }
            fields.budget?.let { put(fields::budget.name, it) }
        }
    }

    fun toMap(fields: ProjectUpdate): Map<String, Any?> {
        val update = mutableMapOf<String, Any?>()

        update["updatedAt"] = Clock.System.now().toJavaInstant()
        fields.name?.let { update[fields::name.name] = it }
        fields.slogan?.let { update[fields::slogan.name] = it }
        fields.identifier?.let { update[fields::identifier.name] = it }
        fields.description?.let { update[fields::description.name] = it }
        fields.avatar?.let { update[fields::avatar.name] = it }
        fields.budget?.let { update[fields::budget.name] = it }
        fields.address?.let { update[fields::address.name] = AddressMapper.toMap(it) }

        return update.filterValues { it != null }
    }

    fun toModel(document: Document): Project {
        return Project(
            id = document.getObjectId("_id").toString(),
            createdAt = DocumentInstantExtractor.extractInstant(document, Project::createdAt.name),
            updatedAt = DocumentInstantExtractor.extractInstant(document, Project::updatedAt.name),
            deletedAt = DocumentInstantExtractor.extractInstantNullable(document, Project::deletedAt.name),
            softDeleted = document.getBoolean(Project::softDeleted.name),
            available = document.getBoolean(Project::available.name),
            name = document.getString(Project::name.name),
            slogan = document.getString(Project::slogan.name),
            identifier = document.getString(Project::identifier.name),
            description = document.getString(Project::description.name),
            avatar = document.getString(Project::avatar.name),
            budget = DocumentDoubleExtractor.extractDoubleOrNull(document, Project::budget.name),
            address = document[Project::address.name]?.let { address -> AddressMapper.toModel(address as Document)}
        )
    }
}