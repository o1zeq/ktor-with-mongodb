package top.dedicado.infrastructure.repositories.principal

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import top.dedicado.domain.models.principal.Session
import top.dedicado.domain.repositories.principal.SessionRepository
import top.dedicado.infrastructure.mappers.principal.SessionMapper
import top.dedicado.infrastructure.plugins.CustomIllegalArgumentException
import top.dedicado.infrastructure.plugins.ForbiddenException
import top.dedicado.presentation.dto.principal.session.SessionCreate
import top.dedicado.presentation.dto.principal.session.SessionUpdate

class SessionRepositoryImp(
    private val collection: MongoCollection<Document>,
): SessionRepository {
    private val mapper = SessionMapper

    override suspend fun findMany(): List<Session> = withContext(Dispatchers.IO) {
        collection.find()
            .sort(Sorts.orderBy(Sorts.descending(Session::timestamp.name)))
            .limit(100)
            .toList().map { document -> mapper.toModel(document) }
    }

    override suspend fun findOne(id: String): Session? = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        collection.find(filter).firstOrNull()?.let { document -> mapper.toModel(document) }
    }

    override suspend fun findOneByRefreshIdToken(refreshIdToken: String): Session? = withContext(Dispatchers.IO) {
        val filter = Filters.eq(Session::refreshIdToken.name, refreshIdToken)

        collection.find(filter).firstOrNull()?.let { document -> mapper.toModel(document) }
    }

    override suspend fun findOneByIssuer(issuer: String): Session? = withContext(Dispatchers.IO) {
        val filter = Filters.eq(Session::issuer.name, issuer)

        collection.find(filter).firstOrNull()?.let { document -> mapper.toModel(document) }
    }

    override suspend fun create(fields: SessionCreate): Session = withContext(Dispatchers.IO) {
        val document = mapper.toDocument(fields)

        collection.insertOne(document)
        mapper.toModel(document)
    }

    override suspend fun update(
        id: String,
        fields: SessionUpdate,
    ): Session = withContext(Dispatchers.IO) {
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

    override suspend fun delete(id: String): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        collection.findOneAndDelete(filter)
            ?: throw ForbiddenException("Nada encontrado por aqui")
    }
}