package top.dedicado.infrastructure.repositories.component

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.types.ObjectId
import top.dedicado.domain.models.component.Activity
import top.dedicado.domain.repositories.component.ActivityRepository
import top.dedicado.infrastructure.mappers.component.ActivityMapper
import top.dedicado.infrastructure.plugins.CustomIllegalArgumentException
import top.dedicado.presentation.dto.component.activity.ActivityCreate

class ActivityRepositoryImp(
    private val collection: MongoCollection<Document>
): ActivityRepository {
    private val mapper = ActivityMapper
    override suspend fun findMany(): List<Activity> = withContext(Dispatchers.IO) {
        collection.find()
            .sort(Sorts.orderBy(Sorts.descending(Activity::timestamp.name)))
            .limit(100)
            .toList().map { document -> mapper.toModel(document) }
    }

    override suspend fun findById(id: String): Activity? = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        collection.find(filter).firstOrNull()?.let { document -> mapper.toModel(document) }
    }

    override suspend fun create(fields: ActivityCreate): Activity = withContext(Dispatchers.IO) {
        val document = mapper.toDocument(fields)

        collection.insertOne(document)
        mapper.toModel(document)
    }

    override suspend fun delete(id: String): Boolean = withContext(Dispatchers.IO) {
        if(!ObjectId.isValid(id)) throw CustomIllegalArgumentException("ID inválido")

        val filter = Filters.eq("_id", ObjectId(id))

        collection.findOneAndDelete(filter) != null
    }
}