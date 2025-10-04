package top.dedicado.presentation.dto.principal.contact

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import top.dedicado.domain.models.Address
import top.dedicado.domain.models.Identification

@Serializable
data class ContactCreate(
    val id: String? = ObjectId().toString(),
    val createdAt: Instant? = Clock.System.now(),
    val updatedAt: Instant? = Clock.System.now(),
    val deletedAt: Instant? = null,
    val softDeleted: Boolean? = false,
    val projectId: String? = null,
    val identifier: String? = null,
    val occupation: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val photo: String? = null,
    val kind: String? = "NETWORK",
    val note: String? = null,
    val identifications: List<Identification>? = emptyList(),
    val address: Address? = null
)
