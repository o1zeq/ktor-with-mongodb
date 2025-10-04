package top.dedicado.domain.models.principal

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Plan

@Serializable
data class Subscription(
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?,
    val softDeleted: Boolean,
    val startsIn: Instant,
    val expiresIn: Instant,
    val status: String,
    val plan: Plan,
    val price: Double,
    val code: String,
    val accountId: String,
    val projectId: String
)
