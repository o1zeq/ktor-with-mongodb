package top.dedicado.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResetPassword(
    val identifier: String,
    val email: String,
)
