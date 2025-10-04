package top.dedicado.presentation.dto.principal.session

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.days

@Serializable
data class SessionUpdate(
    val expiresIn: Instant? = Clock.System.now().plus(7.days),
    val refreshIdToken: String,
    val issuer: String,
)
