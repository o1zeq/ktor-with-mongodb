package top.dedicado.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Signature(
    val name: String,
    val email: String,
    val phone: String?,
    val photo: String?
)
