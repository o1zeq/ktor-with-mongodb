package top.dedicado.infrastructure.mappers

import org.bson.Document
import top.dedicado.utils.extractors.DocumentDoubleExtractor
import top.dedicado.utils.extractors.DocumentFloatExtractor
import top.dedicado.domain.models.Position

object PositionMapper {

    fun toDocument(fields: Position): Document {

        return Document().apply {
            fields.accuracy?.let { put(fields::accuracy.name, it) }
            fields.bearing?.let { put(fields::bearing.name, it) }
            fields.speed?.let { put(fields::speed.name, it) }
            fields.altitude?.let { put(fields::altitude.name, it) }
            put(fields::coordinate.name, fields.coordinate.let { CoordinateMapper.toDocument(it) })
        }
    }

    fun toMap(fields: Position): Map<String, Any?> {
        val update = mutableMapOf<String, Any?>()

        fields.accuracy?.let { update[fields::accuracy.name] = it }
        fields.bearing?.let { update[fields::bearing.name] = it }
        fields.speed?.let { update[fields::speed.name] = it }
        fields.altitude?.let { update[fields::altitude.name] = it }
        fields.coordinate.let { update[fields::coordinate.name] = CoordinateMapper.toMap(it) }

        return update.filterValues { it != null }
    }

    fun toModel(document: Document): Position {

        return Position(
            accuracy = DocumentFloatExtractor.extractFloatOrNull(document, Position::accuracy.name),
            bearing = DocumentFloatExtractor.extractFloatOrNull(document, Position::bearing.name),
            speed = DocumentFloatExtractor.extractFloatOrNull(document, Position::speed.name),
            altitude = DocumentDoubleExtractor.extractDoubleOrNull(document, Position::altitude.name),
            coordinate = document[Position::coordinate.name].let { CoordinateMapper.toModel(it as Document) }
        )
    }
}