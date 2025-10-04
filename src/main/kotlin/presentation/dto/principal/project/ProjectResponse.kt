package top.dedicado.presentation.dto.principal.project

import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Address

@Serializable
data class ProjectResponse(
    val id: String?,
    val available: Boolean?,
    val name: String?,
    val slogan: String?,
    val identity: String?,
    val description: String?,
    val avatar: String?,
    val budget: Double?,
    val address: Address?
)
