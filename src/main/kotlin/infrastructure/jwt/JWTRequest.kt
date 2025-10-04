package top.dedicado.infrastructure.jwt

import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Realm
import top.dedicado.domain.models.Role

@Serializable
data class JWTRequest(
    val aid: String,
    val role: Role,
    val issuer: String,
    val realm: Realm
)
