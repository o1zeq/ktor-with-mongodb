package top.dedicado.infrastructure.repositories.principal

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import top.dedicado.domain.models.principal.Project
import top.dedicado.domain.repositories.principal.ProjectRepository
import top.dedicado.infrastructure.mappers.principal.ProjectMapper
import top.dedicado.infrastructure.plugins.CustomIllegalArgumentException
import top.dedicado.infrastructure.plugins.ForbiddenException
import top.dedicado.presentation.dto.principal.project.ProjectCreate
import top.dedicado.presentation.dto.principal.project.ProjectUpdate

class ProjectRepositoryImp(
    private val collection: MongoCollection<Document>,
): ProjectRepository {
    private val mapper = ProjectMapper

    override suspend fun findMany(): List<Project> = withContext(Dispatchers.IO) {
        collection.find()
            .sort(Sorts.orderBy(Sorts.descending(Project::createdAt.name)))
            .limit(100)
            .toList().map { document -> mapper.toModel(document) }
    }

    override suspend fun findOne(id: String): Project? = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        collection.find(filter).firstOrNull()?.let { document -> mapper.toModel(document) }
    }

    override suspend fun create(fields: ProjectCreate): Project = withContext(Dispatchers.IO) {
        val document = mapper.toDocument(fields)

        collection.insertOne(document)
        mapper.toModel(document)
    }

    override suspend fun update(
        id: String,
        fields: ProjectUpdate,
    ): Project = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val list = mutableListOf<Bson>()
        val data = mapper.toMap(fields).filterValues { it != null }
        data.forEach { (key, value) -> key.isNotEmpty() && list.add(Updates.set(key, value)) }

        if(list.isEmpty())  findOne(id)

        val updates = Updates.combine(list)

        val options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)

        collection.findOneAndUpdate(filter, updates, options)
            ?.let { document -> mapper.toModel(document) }
            ?: throw ForbiddenException("Nada encontrado por aqui")
    }

    override suspend fun updateAvailability(id: String, available: Boolean): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val timestamp = Clock.System.now().toJavaInstant()

        val updates = Updates.combine(
            Updates.set(Project::available.name, available),
            Updates.set(Project::updatedAt.name, timestamp)
        )

        collection.findOneAndUpdate(filter, updates)
            ?: throw ForbiddenException("Nada encontrado por aqui")
    }

    override suspend fun softDelete(id: String): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val timestamp = Clock.System.now().toJavaInstant()

        val updates = Updates.combine(
            Updates.set(Project::updatedAt.name, timestamp),
            Updates.set(Project::deletedAt.name, timestamp),
            Updates.set(Project::softDeleted.name, true),
            Updates.set(Project::available.name, false)
        )

        collection.findOneAndUpdate(filter, updates)
            ?: throw ForbiddenException("Nada encontrado por aqui")
    }

    override suspend fun delete(id: String): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        collection.findOneAndDelete(filter)
            ?: throw ForbiddenException("Nada encontrado por aqui")
    }

    override suspend fun restore(id: String): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val timestamp = Clock.System.now().toJavaInstant()

        val updates = Updates.combine(
            Updates.set(Project::updatedAt.name, timestamp),
            Updates.set(Project::softDeleted.name, false),
            Updates.set(Project::available.name, true)
        )

        collection.findOneAndUpdate(filter, updates)
            ?: throw ForbiddenException("Nada encontrado por aqui")
    }
}