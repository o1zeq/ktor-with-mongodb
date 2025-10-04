package top.dedicado.presentation.dto.principal.subscription

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Plan

@Serializable
data class SubscriptionUpdate(
    val startsIn: Instant? = null,
    val expiresIn: Instant? = null,
    val status: String? = null,
    val plan: Plan? = null,
    val price: Double? = null,
)
