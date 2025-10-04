package top.dedicado.domain.services.principal

import org.koin.core.component.KoinComponent
import top.dedicado.domain.models.principal.Session
import top.dedicado.domain.repositories.principal.SessionRepository
import top.dedicado.presentation.dto.principal.session.SessionCreate
import top.dedicado.presentation.dto.principal.session.SessionUpdate

class SessionService(
    private val sessionRepository: SessionRepository
): KoinComponent {

    suspend fun findMany(): List<Session> = this.sessionRepository.findMany()

    suspend fun findOne(id: String): Session? = this.sessionRepository.findOne(id)

    suspend fun findOneByRefreshIdToken(refreshIdToken: String): Session? = this.sessionRepository.findOneByRefreshIdToken(refreshIdToken)

    suspend fun findOneByIssuer(issuer: String): Session? = this.sessionRepository.findOneByIssuer(issuer)

    suspend fun create(fields: SessionCreate): Session = this.sessionRepository.create(fields)

    suspend fun update(id: String, fields: SessionUpdate): Session {
        return this.sessionRepository.update(id, fields)
    }

    suspend fun delete(id: String): Unit = this.sessionRepository.delete(id)
}