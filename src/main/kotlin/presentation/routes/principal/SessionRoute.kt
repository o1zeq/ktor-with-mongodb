package top.dedicado.presentation.routes.principal

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.get
import top.dedicado.domain.models.Role
import top.dedicado.domain.services.principal.SessionService
import top.dedicado.infrastructure.plugins.authorize

fun Route.sessionRoute() {
    route("/sessions") {
        authenticate("jwt") {
            get("") {
                call.authorize(Role.SUPER)
                val service = call.get<SessionService>()
                val response = service.findMany()

                call.respond(HttpStatusCode.OK, response)
            }

            get("/{id}") {
                call.authorize(Role.SUPER)
                val id = call.parameters["id"]?: return@get call.respond(HttpStatusCode.BadRequest)
                val service = call.get<SessionService>()
                val response = service.findOne(id)?: return@get call.respond(HttpStatusCode.NotFound)

                call.respond(HttpStatusCode.OK, response)
            }

            delete("/{id}") {
                val id = call.parameters["id"]?: return@delete call.respond(HttpStatusCode.BadRequest)
                val service = call.get<SessionService>()
                service.delete(id)

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}