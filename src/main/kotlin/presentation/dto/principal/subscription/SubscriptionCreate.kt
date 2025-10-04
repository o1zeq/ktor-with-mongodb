package top.dedicado.presentation.dto.principal.subscription

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import top.dedicado.domain.models.Plan
import kotlin.time.Duration.Companion.days

@Serializable
data class SubscriptionCreate(
    val id: String? = ObjectId().toString(),
    val createdAt: Instant? = Clock.System.now(),
    val updatedAt: Instant? = Clock.System.now(),
    val deletedAt: Instant? = null,
    val softDeleted: Boolean? = false,
    val startsIn: Instant? = Clock.System.now(),
    val expiresIn: Instant? = Clock.System.now().plus(30.days),
    val status: String? = "ACTIVE",
    val plan: Plan? = Plan.PERSONAL,
    val price: Double? = 0.00,
    val code: String? = Clock.System.now().epochSeconds.toString(),
    val accountId: String,
    val projectId: String
)
