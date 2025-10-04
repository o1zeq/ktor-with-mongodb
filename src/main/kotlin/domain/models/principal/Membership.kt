package top.dedicado.domain.models.principal

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Attribute
import top.dedicado.domain.models.Realm

@Serializable
data class Membership(
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?,
    val softDeleted: Boolean,
    val available: Boolean,
    val attribute: Attribute,
    val realm: Realm,
    val accountId: String,
    val projectId: String,
)
