package top.dedicado.presentation.dto.principal.account

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import top.dedicado.domain.models.Address
import top.dedicado.domain.models.Role

@Serializable
data class AccountCreate(
    val id: String? = ObjectId().toString(),
    val createdAt: Instant? = Clock.System.now(),
    val updatedAt: Instant? = Clock.System.now(),
    val deletedAt: Instant? = null,
    val softDeleted: Boolean? = false,
    val available: Boolean? = true,
    val identity: String? = null,
    val identifier: String,
    val role: Role? = Role.PERSONAL,
    val name: String,
    val email: String,
    val phone: String,
    val photo: String? = null,
    val passHash: String? = null,
    val codeHash: String? = null,
    val address: Address? = null
)
