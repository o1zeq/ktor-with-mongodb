package top.dedicado.utils

import kotlinx.serialization.Serializable

@Serializable
enum class EmailTemplate {
    GENERAL,
    NEW_USER,
    RESET_PASSWORD
}