package top.dedicado.domain.models.principal

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Address

@Serializable
data class Project(
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?,
    val softDeleted: Boolean,
    val available: Boolean,
    val name: String?,
    val slogan: String?,
    val identifier: String?,
    val description: String?,
    val avatar: String?,
    val budget: Double?,
    val address: Address?
)
