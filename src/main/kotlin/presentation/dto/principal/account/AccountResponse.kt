package top.dedicado.presentation.dto.principal.account

import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Address

@Serializable
data class AccountResponse(
    val id: String?,
    val available: Boolean?,
    val name: String,
    val email: String,
    val phone: String,
    val photo: String?,
    val address: Address?
)
