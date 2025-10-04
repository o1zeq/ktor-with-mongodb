package top.dedicado.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePassword(
    val oldPassword: String,
    val newPassword: String
)
