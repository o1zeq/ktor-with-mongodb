package top.dedicado.domain.models.component

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Address

@Serializable
data class Appointment(
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?,
    val softDeleted: Boolean,
    val referenceId: String,
    val personal: Boolean,
    val remind: Boolean,
    val reminded: Boolean,
    val code: String,
    val subject: String,
    val resume: String?,
    val started: Boolean,
    val startedOn: Instant?,
    val finished: Boolean,
    val finishedOn: Instant?,
    val deadline: Instant?,
    val purpose: String,
    val status: String,
    val address: Address?
)
