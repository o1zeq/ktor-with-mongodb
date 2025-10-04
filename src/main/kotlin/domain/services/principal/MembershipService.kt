package top.dedicado.domain.services.principal

import org.koin.core.component.KoinComponent
import top.dedicado.domain.models.principal.Membership
import top.dedicado.domain.repositories.principal.MembershipRepository
import top.dedicado.presentation.dto.principal.membership.MembershipCreate

class MembershipService(
    private val membershipRepository: MembershipRepository
): KoinComponent {

    suspend fun findMany(): List<Membership> = this.membershipRepository.findMany()

    suspend fun findOne(id: String): Membership? = this.membershipRepository.findOne(id)

    suspend fun create(fields: MembershipCreate): Membership = this.membershipRepository.create(fields)

    suspend fun updateAvailability(id: String, available: Boolean): Unit = this.membershipRepository.updateAvailability(id, available)

    suspend fun softDelete(id: String): Unit = this.membershipRepository.softDelete(id)

    suspend fun delete(id: String): Unit = this.membershipRepository.delete(id)

    suspend fun restore(id: String): Unit = this.membershipRepository.restore(id)
}