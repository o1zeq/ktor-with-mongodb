package top.dedicado.domain.repositories.principal

import top.dedicado.domain.models.principal.Contact
import top.dedicado.presentation.dto.principal.contact.ContactCreate
import top.dedicado.presentation.dto.principal.contact.ContactUpdate

interface ContactRepository {

    suspend fun findMany(): List<Contact>
    suspend fun findOne(id: String): Contact?
    suspend fun create(fields: ContactCreate): Contact
    suspend fun update(id: String, fields: ContactUpdate): Contact
    suspend fun softDelete(id: String): Unit
    suspend fun delete(id: String): Unit
    suspend fun restore(id: String): Unit
}