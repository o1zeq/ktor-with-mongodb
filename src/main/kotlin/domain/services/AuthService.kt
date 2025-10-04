package top.dedicado.domain.services

import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import top.dedicado.domain.services.principal.AccountService
import top.dedicado.domain.services.principal.SessionService
import top.dedicado.infrastructure.jwt.JWTProvider
import top.dedicado.infrastructure.jwt.JWTRequest
import top.dedicado.infrastructure.plugins.CustomBadRequestException
import top.dedicado.infrastructure.plugins.ForbiddenException
import top.dedicado.infrastructure.plugins.NotFoundException
import top.dedicado.presentation.dto.auth.AuthRequest
import top.dedicado.presentation.dto.auth.AuthResponse
import top.dedicado.presentation.dto.principal.session.SessionCreate
import top.dedicado.presentation.dto.principal.session.SessionUpdate
import top.dedicado.utils.generators.EncryptorGenerator

class AuthService(
    private val accountService: AccountService,
    private val jwtProvider: JWTProvider,
    private val sessionService: SessionService
): KoinComponent {
    private val encryptorGenerator = EncryptorGenerator

    suspend fun login(request: AuthRequest): AuthResponse {
        val account = this.accountService.findOneByIdentifier(request.identifier)
            ?: throw NotFoundException("Conta não encontrada")

        if (account.softDeleted) throw ForbiddenException("A conta está indisponível")

        val passwordIsValid = account.passHash?.let { passHash ->
            encryptorGenerator.verifyHash(request.password, passHash)
        } ?: throw CustomBadRequestException("A senha não está definida")

        if(!passwordIsValid) throw ForbiddenException("A senha está incorreta")

        val issuer = this.encryptorGenerator.generateHash(account.identifier)

        val jwtRequest = JWTRequest(
            aid = account.id,
            role = account.role,
            issuer = issuer,
            realm = request.realm
        )

        val idToken = this.jwtProvider.generateIdToken(jwtRequest)
        val refreshIdToken = this.jwtProvider.generateRefreshIdToken(issuer)

        val newSession = SessionCreate(
            refreshIdToken = refreshIdToken,
            issuer = issuer,
            accountId = account.id,
            realm = request.realm
        )

        this.sessionService.create(newSession)
        this.accountService.updateAvailability(account.id, true)

        return AuthResponse(
            message = "Boas vindas ${account.name}",
            idToken = idToken,
            refreshIdToken = refreshIdToken
        )
    }

    suspend fun refresh(request: String): AuthResponse {
        val session = this.sessionService.findOneByRefreshIdToken(request)
            ?: throw NotFoundException("Sessão não encontrada")

        val timestamp = Clock.System.now()

        val verifyIdToken = session.let { session ->
            if(session.expiresIn < timestamp)
                throw ForbiddenException("A sessão expirou")

            this.jwtProvider.verifyRefreshIdToken(request, session.issuer)
        }

        val account = this.accountService.findOne(session.accountId)
            ?: throw NotFoundException("Conta não encontrada")

        if (account.softDeleted) throw ForbiddenException("A conta está indisponível")

        val issuerIsValid = this.encryptorGenerator.verifyHash(account.identifier,verifyIdToken.issuer)
        if(!issuerIsValid) throw ForbiddenException("O emissor não está qualificado")

        val issuer = this.encryptorGenerator.generateHash(account.identifier)

        val jwtRequest = JWTRequest(
            aid = account.id,
            role = account.role,
            issuer = issuer,
            realm = session.realm
        )

        val idToken = this.jwtProvider.generateIdToken(jwtRequest)
        val refreshIdToken = this.jwtProvider.generateRefreshIdToken(issuer)

        val updateSession = SessionUpdate(
            refreshIdToken = refreshIdToken,
            issuer = issuer
        )

        this.sessionService.update(session.id, updateSession)

        return AuthResponse(
            message = "Sessão revalidada",
            idToken = idToken,
            refreshIdToken = refreshIdToken
        )
    }

    suspend fun logout(issuer: String): Unit {
        this.sessionService.findOneByIssuer(issuer)?.let { session ->
            this.sessionService.delete(session.id)
            this.accountService.updateAvailability(session.accountId, false)
        }
    }
}