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
import top.dedicado.domain.models.Role
import top.dedicado.domain.models.principal.Account
import top.dedicado.domain.repositories.principal.AccountRepository
import top.dedicado.infrastructure.mappers.principal.AccountMapper
import top.dedicado.infrastructure.plugins.CustomIllegalArgumentException
import top.dedicado.infrastructure.plugins.ForbiddenException
import top.dedicado.presentation.dto.principal.account.AccountCreate
import top.dedicado.presentation.dto.principal.account.AccountUpdate

class AccountRepositoryImp(
    private val collection: MongoCollection<Document>,
): AccountRepository {
    private val mapper = AccountMapper

    override suspend fun findMany(): List<Account> = withContext(Dispatchers.IO) {
        collection.find()
            .sort(Sorts.orderBy(Sorts.descending(Account::createdAt.name)))
            .limit(100)
            .toList().map { document -> mapper.toModel(document) }
    }

    override suspend fun findOne(id: String): Account? = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        collection.find(filter).firstOrNull()?.let { document -> mapper.toModel(document) }
    }

    override suspend fun findOneByIdentifier(identifier: String): Account? = withContext(Dispatchers.IO) {
        val filter = Filters.eq(Account::identifier.name, identifier)

        collection.find(filter).firstOrNull()?.let { document -> mapper.toModel(document) }
    }

    override suspend fun create(fields: AccountCreate): Account = withContext(Dispatchers.IO) {
        val document = mapper.toDocument(fields)

        collection.insertOne(document)
        mapper.toModel(document)
    }

    override suspend fun update(
        id: String,
        fields: AccountUpdate,
    ): Account = withContext(Dispatchers.IO) {
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
            Updates.set(Account::available.name, available),
            Updates.set(Account::updatedAt.name, timestamp)
        )

        collection.findOneAndUpdate(filter, updates)
            ?: throw ForbiddenException("Nada encontrado por aqui")
    }

    override suspend fun updateCodeHash(id: String, codeHash: String): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val timestamp = Clock.System.now().toJavaInstant()

        val updates = Updates.combine(
            Updates.set(Account::codeHash.name, codeHash),
            Updates.set(Account::updatedAt.name, timestamp)
        )

        collection.findOneAndUpdate(filter, updates)
            ?: throw ForbiddenException("Nada encontrado por aqui")
    }

    override suspend fun updatePassHash(id: String, passHash: String): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val timestamp = Clock.System.now().toJavaInstant()

        val updates = Updates.combine(
            Updates.set(Account::updatedAt.name, timestamp),
            Updates.set(Account::passHash.name, passHash),
        )

        collection.findOneAndUpdate(filter, updates)
            ?: throw ForbiddenException("Nada encontrado por aqui")
    }

    override suspend fun updateRole(id: String, role: Role): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val timestamp = Clock.System.now().toJavaInstant()

        val updates = Updates.combine(
            Updates.set(Account::updatedAt.name, timestamp),
            Updates.set(Account::role.name, role.name),
        )

        collection.findOneAndUpdate(filter, updates)
            ?: throw ForbiddenException("Nada encontrado por aqui")

    }

    override suspend fun softDelete(id: String): Unit = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        val timestamp = Clock.System.now().toJavaInstant()

        val updates = Updates.combine(
            Updates.set(Account::updatedAt.name, timestamp),
            Updates.set(Account::deletedAt.name, timestamp),
            Updates.set(Account::softDeleted.name, true),
            Updates.set(Account::available.name, false)
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
            Updates.set(Account::updatedAt.name, timestamp),
            Updates.set(Account::softDeleted.name, false),
            Updates.set(Account::available.name, true)
        )

        collection.findOneAndUpdate(filter, updates)
            ?: throw ForbiddenException("Nada encontrado por aqui")
    }
}