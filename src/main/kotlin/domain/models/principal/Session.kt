package top.dedicado.domain.models.principal

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Realm

@Serializable
data class Session(
    val id: String,
    val timestamp: Instant,
    val expiresIn: Instant,
    val refreshIdToken: String,
    val issuer: String,
    val accountId: String,
    val realm: Realm
)
