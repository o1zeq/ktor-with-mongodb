package top.dedicado.domain.services.principal

import org.koin.core.component.KoinComponent
import top.dedicado.domain.models.Role
import top.dedicado.domain.models.principal.Account
import top.dedicado.domain.repositories.principal.AccountRepository
import top.dedicado.domain.services.ProducerService
import top.dedicado.infrastructure.plugins.CustomBadRequestException
import top.dedicado.infrastructure.plugins.ForbiddenException
import top.dedicado.infrastructure.plugins.NotFoundException
import top.dedicado.infrastructure.plugins.UnauthorizedException
import top.dedicado.presentation.dto.ResetPassword
import top.dedicado.presentation.dto.UpdatePassword
import top.dedicado.presentation.dto.principal.account.AccountCreate
import top.dedicado.presentation.dto.principal.account.AccountResponse
import top.dedicado.presentation.dto.principal.account.AccountUpdate
import top.dedicado.presentation.dto.sender.SendEmail
import top.dedicado.utils.EmailTemplate
import top.dedicado.utils.generators.EncryptorGenerator
import top.dedicado.utils.generators.RandomStringGenerator

class AccountService(
    private val accountRepository: AccountRepository,
    private val producerService: ProducerService,
): KoinComponent {
    private val encryptorGenerator = EncryptorGenerator
    private val randomStringGenerator = RandomStringGenerator

    suspend fun findMany(): List<Account> = this.accountRepository.findMany()

    suspend fun findMe(id: String): AccountResponse {
        val account = this.accountRepository.findOne(id)?: throw NotFoundException("Conta não encontrada")

        return AccountResponse(
            id = account.id,
            available = account.available,
            name = account.name,
            email = account.email,
            phone = account.phone,
            photo = account.photo,
            address = account.address
        )
    }

    suspend fun findOne(id: String): Account? = this.accountRepository.findOne(id)

    suspend fun findOneByIdentifier(identifier: String): Account? = this.accountRepository.findOneByIdentifier(identifier)

    suspend fun create(fields: AccountCreate): Account = this.accountRepository.create(fields)

    suspend fun update(id: String, fields: AccountUpdate): Account {
        return this.accountRepository.update(id, fields)
    }

    suspend fun updateAvailability(id: String, available: Boolean): Unit = this.accountRepository.updateAvailability(id, available)

    suspend fun updatePassword(id: String, fields: UpdatePassword): String {
        val account = this.accountRepository.findOne(id)?: throw NotFoundException("Conta não encontrada")

        if(account.softDeleted) throw UnauthorizedException("A conta está indisponível")

        val passwordIsValid = account.passHash?.let { passHash ->
            encryptorGenerator.verifyHash(fields.oldPassword, passHash)
        } ?: throw CustomBadRequestException("A senha não está definida")

        if(!passwordIsValid) throw ForbiddenException("A senha antiga está incorreta")

        val passHash = encryptorGenerator.generateHash(fields.newPassword)
        this.accountRepository.updatePassHash(id, passHash)

        return "${account.name}, sua senha foi atualizada com sucesso. Inicie uma nova sessão para continuar"
    }

    suspend fun resetPassword(fields: ResetPassword): String {
        val account = this.accountRepository.findOneByIdentifier(fields.identifier)
            ?: throw NotFoundException("Conta não encontrada")

        if (account.softDeleted) throw UnauthorizedException("A conta está indisponível")
        if (account.email != fields.email) throw UnauthorizedException("Informações incompatíveis com a conta")

        val password = randomStringGenerator.generatePassword(8)
        val code = randomStringGenerator.generateNumericCode(6)

        val passHash = encryptorGenerator.generateHash(password)
        val codeHash = encryptorGenerator.generateHash(code)

        this.accountRepository.updatePassHash(account.id, passHash)
        this.accountRepository.updateCodeHash(account.id, codeHash)

        val sendEmail = SendEmail(
            to = fields.email,
            subject = "Redefinição de Senha",
            template = EmailTemplate.RESET_PASSWORD,
            attributes = mapOf(
                "name" to account.name,
                "password" to password
            )
        )
        this.producerService.sendEmail(sendEmail)

        return "Senha redefinida e enviada para o e-mail ${fields.email}"
    }

    suspend fun updateRole(id: String, role: Role): Unit = this.accountRepository.updateRole(id, role)

    suspend fun softDelete(id: String): Unit = this.accountRepository.softDelete(id)

    suspend fun delete(id: String): Unit = this.accountRepository.delete(id)

    suspend fun restore(id: String): Unit = this.accountRepository.restore(id)
}