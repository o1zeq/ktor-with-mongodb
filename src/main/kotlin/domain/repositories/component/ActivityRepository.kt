package top.dedicado.domain.repositories.component

import top.dedicado.domain.models.component.Activity
import top.dedicado.presentation.dto.component.activity.ActivityCreate

interface ActivityRepository {
    suspend fun findMany(): List<Activity>
    suspend fun findById(id: String): Activity?
    suspend fun create(fields: ActivityCreate): Activity
    suspend fun delete(id: String): Boolean
}