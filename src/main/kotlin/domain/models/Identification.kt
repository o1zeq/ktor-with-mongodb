package top.dedicado.domain.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Identification(
    val kind: String,
    val code: String,
    val issuer: String?,
    val placeOfIssue: String?,
    val issuedOn: Instant?,
    val validUntil: Instant?,
    val photo: String?,
    val fingerprint: String?,
)
