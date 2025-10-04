package top.dedicado.domain.repositories.principal

import top.dedicado.domain.models.principal.Subscription
import top.dedicado.presentation.dto.principal.subscription.SubscriptionCreate
import top.dedicado.presentation.dto.principal.subscription.SubscriptionUpdate

interface SubscriptionRepository {
    suspend fun findMany(): List<Subscription>
    suspend fun findOne(id: String): Subscription?
    suspend fun create(fields: SubscriptionCreate): Subscription
    suspend fun update(id: String, fields: SubscriptionUpdate): Subscription
    suspend fun softDelete(id: String): Unit
    suspend fun delete(id: String): Unit
    suspend fun restore(id: String): Unit
}