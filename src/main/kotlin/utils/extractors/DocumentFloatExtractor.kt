package top.dedicado.utils.extractors

import org.bson.Document

object DocumentFloatExtractor {

    fun extractFloat(document: Document, field: String): Float {
        return when (val value = document[field]) {
            is Float -> value
            is String -> value.toFloatOrNull() ?: 0.0f
            is Number -> value.toFloat()
            null -> 0.0f
            else -> throw IllegalArgumentException("Cannot convert $field value '$value' of type ${value::class.simpleName} to Float")
        }
    }

    fun extractFloatOrNull(document: Document, field: String): Float? {
        return when (val value = document[field]) {
            is Float -> value
            is String -> value.toFloatOrNull()
            is Number -> value.toFloat()
            null -> null
            else -> throw IllegalArgumentException("Cannot convert $field value '$value' of type ${value::class.simpleName} to Float")
        }
    }
}