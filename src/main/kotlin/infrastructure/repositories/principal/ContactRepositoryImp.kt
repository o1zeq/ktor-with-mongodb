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
import top.dedicado.domain.models.principal.Contact
import top.dedicado.domain.repositories.principal.ContactRepository
import top.dedicado.infrastructure.mappers.principal.ContactMapper
import top.dedicado.infrastructure.plugins.CustomIllegalArgumentException
import top.dedicado.infrastructure.plugins.ForbiddenException
import top.dedicado.presentation.dto.principal.contact.ContactCreate
import top.dedicado.presentation.dto.principal.contact.ContactUpdate

class ContactRepositoryImp(
    private val collection: MongoCollection<Document>,
): ContactRepository {
    private val mapper = ContactMapper

    override suspend fun findMany(): List<Contact> = withContext(Dispatchers.IO) {
        collection.find()
            .sort(Sorts.orderBy(Sorts.descending(Contact::createdAt.name)))
            .limit(100)
            .toList().map { document -> mapper.toModel(document) }
    }

    override suspend fun findOne(id: String): Contact? = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        collection.find(filter).firstOrNull()?.let { document -> mapper.toModel(document) }
    }

    override suspend fun create(fields: ContactCreate): Contact = withContext(Dispatchers.IO) {
        val document = mapper.toDocument(fields)

        collection.insertOne(document)
        mapper.toModel(document)
    }

    override suspend fun update(
        id: String,
        fields: ContactUpdate,
    ): Contact = withContext(Dispatchers.IO) {
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

    override suspend fun softDelete(id: String): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val timestamp = Clock.System.now().toJavaInstant()

        val updates = Updates.combine(
            Updates.set(Contact::updatedAt.name, timestamp),
            Updates.set(Contact::deletedAt.name, timestamp),
            Updates.set(Contact::softDeleted.name, true),
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
            Updates.set(Contact::updatedAt.name, timestamp),
            Updates.set(Contact::softDeleted.name, false),
        )

        collection.findOneAndUpdate(filter, updates)
            ?: throw ForbiddenException("Nada encontrado por aqui")
    }
}