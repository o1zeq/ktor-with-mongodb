package top.dedicado.presentation.dto.component.activity

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import top.dedicado.domain.models.Position
import top.dedicado.domain.models.Signature

@Serializable
data class ActivityCreate(
    val id: String? = ObjectId().toString(),
    val timestamp: Instant? = Clock.System.now(),
    val referenceId: String? = null,
    val context: String? = null,
    val action: String? = null,
    val evidence: String? = null,
    val position: Position? = null,
    val signature: Signature? = null
)
