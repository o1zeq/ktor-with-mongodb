package top.dedicado.presentation.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.get
import top.dedicado.domain.services.AuthService
import top.dedicado.infrastructure.plugins.getIssuerFromPrincipal
import top.dedicado.presentation.dto.auth.AuthRequest

fun Route.authRoute() {
    route("/auth") {
        post("") {
            val request = call.receive<AuthRequest>()
            val service = call.get<AuthService>()
            val response = service.login(request)

            call.respond(HttpStatusCode.Created, response)
        }

        post("/refresh") {
            val request = call.queryParameters["refreshIdToken"] ?: return@post call.respond(HttpStatusCode.Unauthorized)
            val service = call.get<AuthService>()
            val response = service.refresh(request)

            call.respond(HttpStatusCode.Created, response)
        }

        authenticate("jwt") {
            post("/logout") {
                val issuer = call.getIssuerFromPrincipal()
                val service = call.get<AuthService>()
                service.logout(issuer)

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}