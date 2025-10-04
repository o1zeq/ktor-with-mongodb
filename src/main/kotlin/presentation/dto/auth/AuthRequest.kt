package top.dedicado.presentation.dto.auth

import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Realm

@Serializable
data class AuthRequest(
    val identifier: String,
    val password: String,
    val realm: Realm,
)
