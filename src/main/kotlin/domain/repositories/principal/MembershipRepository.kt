package top.dedicado.domain.repositories.principal

import top.dedicado.domain.models.principal.Membership
import top.dedicado.presentation.dto.principal.membership.MembershipCreate

interface MembershipRepository {
    suspend fun findMany(): List<Membership>
    suspend fun findOne(id: String): Membership?
    suspend fun create(fields: MembershipCreate): Membership
    suspend fun updateAvailability(id: String, available: Boolean): Unit
    suspend fun softDelete(id: String): Unit
    suspend fun delete(id: String): Unit
    suspend fun restore(id: String): Unit
}