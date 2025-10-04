package top.dedicado.infrastructure.repositories.principal

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.bson.Document
import org.bson.types.ObjectId
import top.dedicado.domain.models.principal.Membership
import top.dedicado.domain.repositories.principal.MembershipRepository
import top.dedicado.infrastructure.mappers.principal.MembershipMapper
import top.dedicado.infrastructure.plugins.CustomIllegalArgumentException
import top.dedicado.infrastructure.plugins.ForbiddenException
import top.dedicado.presentation.dto.principal.membership.MembershipCreate

class MembershipRepositoryImp(
    private val collection: MongoCollection<Document>,
): MembershipRepository {
    private val mapper = MembershipMapper

    override suspend fun findMany(): List<Membership> = withContext(Dispatchers.IO) {
        collection.find()
            .sort(Sorts.orderBy(Sorts.descending(Membership::createdAt.name)))
            .limit(100)
            .toList().map { document -> mapper.toModel(document) }
    }

    override suspend fun findOne(id: String): Membership? = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        collection.find(filter).firstOrNull()?.let { document -> mapper.toModel(document) }
    }

    override suspend fun create(fields: MembershipCreate): Membership = withContext(Dispatchers.IO) {
        val document = mapper.toDocument(fields)

        collection.insertOne(document)
        mapper.toModel(document)
    }

    override suspend fun updateAvailability(id: String, available: Boolean) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val timestamp = Clock.System.now().toJavaInstant()

        val updates = Updates.combine(
            Updates.set(Membership::updatedAt.name, timestamp),
            Updates.set(Membership::available.name, available)
        )

        collection.findOneAndUpdate(filter, updates)
            ?:throw ForbiddenException("Nada encontrado por aqui")
    }

    override suspend fun softDelete(id: String): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val timestamp = Clock.System.now().toJavaInstant()

        val updates = Updates.combine(
            Updates.set(Membership::updatedAt.name, timestamp),
            Updates.set(Membership::deletedAt.name, timestamp),
            Updates.set(Membership::softDeleted.name, true),
            Updates.set(Membership::available.name, false)
            )

        collection.findOneAndUpdate(filter, updates)
            ?:throw ForbiddenException("Nada encontrado por aqui")
    }

    override suspend fun delete(id: String): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        collection.findOneAndDelete(filter)
            ?:throw ForbiddenException("Nada encontrado por aqui")

    }

    override suspend fun restore(id: String): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val timestamp = Clock.System.now().toJavaInstant()

        val updates = Updates.combine(
            Updates.set(Membership::updatedAt.name, timestamp),
            Updates.set(Membership::softDeleted.name, false),
            Updates.set(Membership::available.name, true)
        )

        collection.findOneAndUpdate(filter, updates)
            ?:throw ForbiddenException("Nada encontrado por aqui")
    }
}