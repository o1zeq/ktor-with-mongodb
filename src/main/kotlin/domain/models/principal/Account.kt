package top.dedicado.domain.models.principal

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import top.dedicado.domain.models.Address
import top.dedicado.domain.models.Role

@Serializable
data class Account(
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?,
    val softDeleted: Boolean,
    val available: Boolean,
    val identity: String?,
    val identifier: String,
    val role: Role,
    val name: String,
    val email: String,
    val phone: String,
    val photo: String?,
    val passHash: String?,
    val codeHash: String?,
    val address: Address?
)
