package top.dedicado.presentation.dto.principal.account

import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Address

@Serializable
data class AccountUpdate(
    val identity: String? = null,
    val identifier: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    val address: Address? = null
)
