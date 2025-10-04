package top.dedicado.presentation.dto.principal.contact

import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Address
import top.dedicado.domain.models.Identification

@Serializable
data class ContactUpdate(
    val identifier: String? = null,
    val occupation: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val photo: String? = null,
    val kind: String? = null,
    val note: String? = null,
    val identifications: List<Identification>? = null,
    val address: Address? = null
)
