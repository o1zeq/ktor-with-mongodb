package top.dedicado.presentation.dto.principal.project

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import top.dedicado.domain.models.Address

@Serializable
data class ProjectCreate(
    val id: String? = ObjectId().toString(),
    val createdAt: Instant? = Clock.System.now(),
    val updatedAt: Instant? = Clock.System.now(),
    val deletedAt: Instant? = null,
    val softDeleted: Boolean? = false,
    val available: Boolean? = true,
    val name: String? = null,
    val slogan: String? = null,
    val identifier: String? = null,
    val description: String? = null,
    val avatar: String? = null,
    val budget: Double? = 0.00,
    val address: Address? = null
)
