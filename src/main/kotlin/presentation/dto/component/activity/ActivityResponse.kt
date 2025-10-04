package top.dedicado.presentation.dto.component.activity

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Position
import top.dedicado.domain.models.Signature

@Serializable
data class ActivityResponse(
    val timestamp: Instant,
    val context: String?,
    val action: String?,
    val evidence: String?,
    val position: Position?,
    val signature: Signature?
)
