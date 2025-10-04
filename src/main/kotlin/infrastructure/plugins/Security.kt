package top.dedicado.infrastructure.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.server.response.respond
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject
import top.dedicado.infrastructure.secrets.SecretKeys
import kotlin.collections.mapOf

fun Application.configureSecurity() {
    val secretKeys by inject<SecretKeys>()

    val secret = secretKeys.jwtSecret

    val config = environment.config
    val realm = config.property("jwt.realm").getString()
    val audience = config.property("jwt.audience").getString()

    authentication {
        jwt("jwt") {
            realm

            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .build()
            )


            authHeader { call ->
                val header = call.request.headers["Authorization"]
                val query = call.request.queryParameters["id_token"]

                when {
                    header != null -> parseAuthorizationHeader(header)
                    query != null -> parseAuthorizationHeader("Bearer $query")
                    else -> null
                }
            }

            validate { credential ->
                if (credential.payload.getClaim("uid").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { defaultScheme, realm ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Token inválido ou expirado")
                )
            }
        }
    }
}