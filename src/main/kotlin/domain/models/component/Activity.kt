package top.dedicado.domain.models.component

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Position
import top.dedicado.domain.models.Signature

@Serializable
data class Activity(
    val id: String,
    val timestamp: Instant,
    val referenceId: String,
    val context: String?,
    val action: String?,
    val evidence: String?,
    val position: Position?,
    val signature: Signature?
)
