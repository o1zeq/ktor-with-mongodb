package top.dedicado.domain.repositories.principal

import top.dedicado.domain.models.principal.Project
import top.dedicado.presentation.dto.principal.project.ProjectCreate
import top.dedicado.presentation.dto.principal.project.ProjectUpdate

interface ProjectRepository {
    suspend fun findMany(): List<Project>
    suspend fun findOne(id: String): Project?
    suspend fun create(fields: ProjectCreate): Project
    suspend fun update(id: String, fields: ProjectUpdate): Project
    suspend fun updateAvailability(id: String, available: Boolean): Unit
    suspend fun softDelete(id: String): Unit
    suspend fun delete(id: String): Unit
    suspend fun restore(id: String): Unit
}