package top.dedicado.domain.repositories.principal

import top.dedicado.domain.models.Role
import top.dedicado.domain.models.principal.Account
import top.dedicado.presentation.dto.principal.account.AccountCreate
import top.dedicado.presentation.dto.principal.account.AccountUpdate

interface AccountRepository {
    suspend fun findMany(): List<Account>
    suspend fun findOne(id: String): Account?
    suspend fun findOneByIdentifier(identifier: String): Account?
    suspend fun create(fields: AccountCreate): Account
    suspend fun update(id: String, fields: AccountUpdate): Account
    suspend fun updateAvailability(id: String, available: Boolean): Unit
    suspend fun updateCodeHash(id: String, codeHash: String): Unit
    suspend fun updatePassHash(id: String, passHash: String): Unit
    suspend fun updateRole(id: String, role: Role): Unit
    suspend fun softDelete(id: String): Unit
    suspend fun delete(id: String): Unit
    suspend fun restore(id: String): Unit
}