package top.dedicado.infrastructure.mappers

import org.bson.Document
import top.dedicado.utils.extractors.DocumentDoubleExtractor
import top.dedicado.domain.models.Coordinate

object CoordinateMapper {

    fun toDocument(fields: Coordinate): Document {

        return Document().apply {
            put(fields::latitude.name, fields.latitude)
            put(fields::longitude.name, fields.longitude)
        }
    }

    fun toMap(fields: Coordinate): Map<String, Any?> {
        val update = mutableMapOf<String, Any?>()

        update[fields::latitude.name] = fields.latitude
        update[fields::longitude.name] = fields.longitude

        return update.filterValues { it != null }
    }

    fun toModel(document: Document): Coordinate {

        return Coordinate(
            latitude = DocumentDoubleExtractor.extractDouble(document,Coordinate::latitude.name),
            longitude = DocumentDoubleExtractor.extractDouble(document,Coordinate::longitude.name),
        )
    }
}