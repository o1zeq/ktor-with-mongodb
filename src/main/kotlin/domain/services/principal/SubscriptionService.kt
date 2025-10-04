package top.dedicado.domain.services.principal

import org.koin.core.component.KoinComponent
import top.dedicado.domain.models.principal.Subscription
import top.dedicado.domain.repositories.principal.SubscriptionRepository
import top.dedicado.presentation.dto.principal.subscription.SubscriptionCreate
import top.dedicado.presentation.dto.principal.subscription.SubscriptionUpdate

class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository
): KoinComponent {

    suspend fun findMany(): List<Subscription> = this.subscriptionRepository.findMany()

    suspend fun findOne(id: String): Subscription? = this.subscriptionRepository.findOne(id)

    suspend fun create(fields: SubscriptionCreate): Subscription = this.subscriptionRepository.create(fields)

    suspend fun update(id: String, fields: SubscriptionUpdate): Subscription {
        return this.subscriptionRepository.update(id, fields)
    }

    suspend fun softDelete(id: String): Unit = this.subscriptionRepository.softDelete(id)

    suspend fun delete(id: String): Unit = this.subscriptionRepository.delete(id)

    suspend fun restore(id: String): Unit = this.subscriptionRepository.restore(id)
}