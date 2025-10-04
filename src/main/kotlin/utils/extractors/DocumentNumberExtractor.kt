package top.dedicado.utils.extractors

import org.bson.Document

object DocumentNumberExtractor {

    inline fun <reified T : Number> extractNumber(document: Document, field: String): T? {
        return when (val value = document[field]) {
            is T -> value
            is String -> when (T::class) {
                Float::class -> value.toFloatOrNull() as? T
                Double::class -> value.toDoubleOrNull() as? T
                else -> null
            }
            is Number -> when (T::class) {
                Float::class -> value.toFloat() as T
                Double::class -> value.toDouble() as T
                else -> null
            }
            null -> null
            else -> throw IllegalArgumentException("Cannot convert $field value '$value' to ${T::class.simpleName}")
        }
    }
}