package top.dedicado.presentation.dto.principal.contact

import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Address

@Serializable
data class ContactResponse(
    val id: String?,
    val identity: String?,
    val name: String?,
    val phone: String?,
    val email: String?,
    val photo: String?,
    val kind: String?,
    val note: String?,
    val address: Address?
)
