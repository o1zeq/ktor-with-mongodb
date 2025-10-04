package top.dedicado.domain.repositories.principal

import top.dedicado.domain.models.principal.Session
import top.dedicado.presentation.dto.principal.session.SessionCreate
import top.dedicado.presentation.dto.principal.session.SessionUpdate

interface SessionRepository {
    suspend fun findMany(): List<Session>
    suspend fun findOne(id: String): Session?
    suspend fun findOneByRefreshIdToken(refreshIdToken: String): Session?
    suspend fun findOneByIssuer(issuer: String): Session?
    suspend fun create(fields: SessionCreate): Session
    suspend fun update(id: String, fields: SessionUpdate): Session
    suspend fun delete(id: String): Unit
}