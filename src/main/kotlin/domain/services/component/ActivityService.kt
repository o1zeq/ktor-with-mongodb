package top.dedicado.domain.services.component

import org.koin.core.component.KoinComponent
import top.dedicado.domain.models.component.Activity
import top.dedicado.domain.repositories.component.ActivityRepository
import top.dedicado.presentation.dto.component.activity.ActivityCreate

class ActivityService(
    private val activityRepository: ActivityRepository
): KoinComponent {

    suspend fun findMany(): List<Activity> = this.activityRepository.findMany()

    suspend fun findById(id: String): Activity? = this.activityRepository.findById(id)

    suspend fun create(fields: ActivityCreate): Activity = this.activityRepository.create(fields)

    suspend fun delete(id: String): Boolean = this.activityRepository.delete(id)
}