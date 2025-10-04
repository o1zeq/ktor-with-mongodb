package top.dedicado.presentation.dto.principal.membership

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import top.dedicado.domain.models.Attribute
import top.dedicado.domain.models.Realm

@Serializable
data class MembershipCreate(
    val id: String? = ObjectId().toString(),
    val createdAt: Instant? = Clock.System.now(),
    val updatedAt: Instant? = Clock.System.now(),
    val deletedAt: Instant? = null,
    val softDeleted: Boolean? = false,
    val available: Boolean? = true,
    val isActive: Boolean? = true,
    val attribute: Attribute? = Attribute.GUEST,
    val realm: Realm,
    val accountId: String,
    val projectId: String,
)
