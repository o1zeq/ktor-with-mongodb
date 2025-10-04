package top.dedicado.domain.models.principal

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Address
import top.dedicado.domain.models.Identification

@Serializable
data class Contact(
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?,
    val softDeleted: Boolean,
    val projectId: String,
    val identifier: String?,
    val occupation: String?,
    val name: String?,
    val phone: String?,
    val email: String?,
    val photo: String?,
    val kind: String,
    val note: String?,
    val identifications: List<Identification>?,
    val address: Address?
)
