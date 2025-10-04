package top.dedicado.domain.services.principal

import org.koin.core.component.KoinComponent
import top.dedicado.domain.models.principal.Contact
import top.dedicado.domain.repositories.principal.ContactRepository
import top.dedicado.presentation.dto.principal.contact.ContactCreate
import top.dedicado.presentation.dto.principal.contact.ContactUpdate

class ContactService(
    private val contactRepository: ContactRepository
): KoinComponent {

    suspend fun findMany(): List<Contact> = this.contactRepository.findMany()

    suspend fun findOne(id: String): Contact? = this.contactRepository.findOne(id)

    suspend fun create(fields: ContactCreate): Contact = this.contactRepository.create(fields)

    suspend fun update(id: String, fields: ContactUpdate): Contact {
        return this.contactRepository.update(id, fields)
    }

    suspend fun softDelete(id: String): Unit = this.contactRepository.softDelete(id)

    suspend fun delete(id: String): Unit = this.contactRepository.delete(id)

    suspend fun restore(id: String): Unit = this.contactRepository.restore(id)
}