package top.dedicado.presentation.dto.register

import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Realm

@Serializable
data class RegisterRequest(
    val identifier: String,
    val name: String,
    val email: String,
    val phone: String,
    val realm: Realm,
)
