package top.dedicado.presentation.dto.principal.session

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import top.dedicado.domain.models.Realm
import kotlin.time.Duration.Companion.days

@Serializable
data class SessionCreate(
    val id: String? = ObjectId().toString(),
    val timestamp: Instant? = Clock.System.now(),
    val expiresIn: Instant? = Clock.System.now().plus(7.days),
    val refreshIdToken: String,
    val issuer: String,
    val accountId: String,
    val realm: Realm
)
