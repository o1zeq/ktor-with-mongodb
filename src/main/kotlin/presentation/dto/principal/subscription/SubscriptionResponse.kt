package top.dedicado.presentation.dto.principal.subscription

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Plan

@Serializable
data class SubscriptionResponse(
    val id: String?,
    val startsIn: Instant?,
    val expiresIn: Instant?,
    val status: String?,
    val plan: Plan?,
    val price: Double?,
    val code: String?,
)
