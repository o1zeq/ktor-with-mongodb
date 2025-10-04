package top.dedicado.domain.services

import org.bson.types.ObjectId
import org.koin.core.component.KoinComponent
import top.dedicado.domain.services.principal.AccountService
import top.dedicado.presentation.dto.principal.account.AccountCreate
import top.dedicado.presentation.dto.register.RegisterRequest
import top.dedicado.presentation.dto.sender.SendEmail
import top.dedicado.utils.EmailTemplate
import top.dedicado.utils.generators.EncryptorGenerator
import top.dedicado.utils.generators.RandomStringGenerator

class RegisterService(
    private val accountService: AccountService,
    private val producerService: ProducerService,
): KoinComponent {
    private val encryptorGenerator = EncryptorGenerator
    private val randomStringGenerator = RandomStringGenerator

    suspend fun insert(fields: RegisterRequest): String {
        val password = randomStringGenerator.generatePassword(8)
        val code = randomStringGenerator.generateNumericCode(6)

        val passHash = encryptorGenerator.generateHash(password)
        val codeHash = encryptorGenerator.generateHash(code)

        val accountId = ObjectId().toString()
        val newAccount = AccountCreate(
            id = accountId,
            identifier = fields.identifier,
            name = fields.name,
            email = fields.email,
            phone = fields.phone,
            passHash = passHash,
            codeHash = codeHash
        )
        this.accountService.create(newAccount)

        val sendEmail = SendEmail(
            to = fields.email,
            subject = "Boas Vindas da Dedicado",
            template = EmailTemplate.NEW_USER,
            attributes = mapOf(
                "name" to fields.name,
                "identifier" to fields.identifier,
                "password" to password
            )
        )
        this.producerService.sendEmail(sendEmail)

        return "${fields.name}, recebemos o seu registro! Por favor, verifique o seu e-mail ${fields.email} para mais informações."
    }
}