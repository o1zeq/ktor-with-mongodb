package top.dedicado.utils.extractors

import org.bson.Document

object  DocumentDoubleExtractor {

    fun extractDouble(document: Document, field: String): Double {
        return when (val value = document[field]) {
            is Double -> value
            is String -> value.toDoubleOrNull() ?: 0.0
            is Number -> value.toDouble()
            null -> 0.0
            else -> throw IllegalArgumentException("Cannot convert $field value '$value' of type ${value::class.simpleName} to Double")
        }
    }

    fun extractDoubleOrNull(document: Document, field: String): Double? {
        return when (val value = document[field]) {
            is Double -> value
            is String -> value.toDoubleOrNull()
            is Number -> value.toDouble()
            null -> null
            else -> throw IllegalArgumentException("Cannot convert $field value '$value' of type ${value::class.simpleName} to Double")
        }
    }
}