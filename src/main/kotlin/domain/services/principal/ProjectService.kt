package top.dedicado.domain.services.principal

import org.koin.core.component.KoinComponent
import top.dedicado.domain.models.principal.Project
import top.dedicado.domain.repositories.principal.ProjectRepository
import top.dedicado.presentation.dto.principal.project.ProjectCreate
import top.dedicado.presentation.dto.principal.project.ProjectUpdate

class ProjectService(
    private val projectRepository: ProjectRepository
): KoinComponent {

    suspend fun findMany(): List<Project> = this.projectRepository.findMany()

    suspend fun findOne(id: String): Project? = this.projectRepository.findOne(id)

    suspend fun create(fields: ProjectCreate): Project = this.projectRepository.create(fields)

    suspend fun update(id: String, fields: ProjectUpdate): Project {
        return this.projectRepository.update(id, fields)
    }

    suspend fun updateAvailability(id: String, available: Boolean): Unit = this.projectRepository.updateAvailability(id, available)

    suspend fun softDelete(id: String): Unit = this.projectRepository.softDelete(id)

    suspend fun delete(id: String): Unit = this.projectRepository.delete(id)

    suspend fun restore(id: String): Unit = this.projectRepository.restore(id)
}