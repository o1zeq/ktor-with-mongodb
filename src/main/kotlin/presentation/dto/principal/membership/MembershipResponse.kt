package top.dedicado.presentation.dto.principal.membership

import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Attribute

@Serializable
data class MembershipResponse(
    val id: String?,
    val available: Boolean,
    val isActive: Boolean,
    val attribute: Attribute,
    val realm: String,
)