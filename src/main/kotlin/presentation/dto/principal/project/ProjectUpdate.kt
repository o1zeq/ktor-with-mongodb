package top.dedicado.presentation.dto.principal.project

import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Address

@Serializable
data class ProjectUpdate(
    val name: String? = null,
    val slogan: String? = null,
    val identifier: String? = null,
    val description: String? = null,
    val avatar: String? = null,
    val budget: Double? = null,
    val address: Address? = null
)
