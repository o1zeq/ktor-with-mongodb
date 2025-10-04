package top.dedicado.utils.extractors

import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import org.bson.Document
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DocumentInstantExtractor {

    fun extractInstant(document: Document, field: String): Instant {
        return when (val value = document[field]) {
            is Date -> {
                value.toInstant().toKotlinInstant()
            }
            is java.time.Instant -> {
                value.toKotlinInstant()
            }
            is String -> {
                parseStringToInstant(value)
            }
            is Long -> {
                Instant.fromEpochMilliseconds(value)
            }
            else -> {
                println("Valor inesperado para $field: $value (${value?.javaClass})")
                Instant.fromEpochMilliseconds(System.currentTimeMillis())
            }
        }
    }

    fun extractInstantNullable(document: Document, field: String): Instant? {
        val value = document[field] ?: return null
        return when (value) {
            is Date -> value.toInstant().toKotlinInstant()
            is java.time.Instant -> value.toKotlinInstant()
            is String -> parseStringToInstant(value)
            is Long -> Instant.fromEpochMilliseconds(value)
            else -> {
                println("Valor inesperado para $field: $value (${value.javaClass})")
                null
            }
        }
    }

    private fun parseStringToInstant(dateString: String): Instant {
        return try {
            Instant.parse(dateString)
        } catch (e: Exception) {
            try {
                val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                val date = formatter.parse(dateString)
                date.toInstant().toKotlinInstant()
            } catch (e2: Exception) {
                println("Erro ao parsear data: $dateString. Erro: ${e2.message}")
                Instant.fromEpochMilliseconds(System.currentTimeMillis())
            }
        }
    }
}