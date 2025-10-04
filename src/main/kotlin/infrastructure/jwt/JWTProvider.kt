package top.dedicado.infrastructure.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.server.config.ApplicationConfig
import top.dedicado.infrastructure.plugins.ForbiddenException
import top.dedicado.infrastructure.secrets.SecretKeys
import java.util.Date
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

class JWTProvider(
    private val applicationConfig: ApplicationConfig,
    private val secretKeys: SecretKeys
) {
    private val audience = this.applicationConfig.property("jwt.audience").getString()
    private val secret = this.secretKeys.jwtSecret

    fun generateIdToken(request: JWTRequest): String {
        val expiresAt = Date(System.currentTimeMillis() + 15.minutes.inWholeMilliseconds)

        return JWT.create()
            .withAudience(audience)
            .withClaim("aid", request.aid)
            .withClaim("role", request.role.name)
            .withClaim("realm", request.realm.name)
            .withIssuer(request.issuer)
            .withExpiresAt(expiresAt)
            .sign(Algorithm.HMAC256(secret))
    }

    fun generateRefreshIdToken(issuer: String): String {
        val expiresAt = Date(System.currentTimeMillis() + 7.days.inWholeMilliseconds)

        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(expiresAt)
            .sign(Algorithm.HMAC256(secret))
    }

    fun verifyRefreshIdToken(token: String, issuer: String): DecodedJWT {
        val verifier = JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

        return verifier.verify(token)?: throw ForbiddenException("Token inválido")
    }
}