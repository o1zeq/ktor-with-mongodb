package top.dedicado.presentation.routes.principal

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.get
import top.dedicado.domain.models.Role
import top.dedicado.domain.services.principal.ProjectService
import top.dedicado.infrastructure.plugins.authorize
import top.dedicado.presentation.dto.principal.project.ProjectCreate
import top.dedicado.presentation.dto.principal.project.ProjectUpdate

fun Route.projectRoute() {
    route("/projects") {
        authenticate("jwt") {
            get("") {
                call.authorize(Role.SUPER)
                val service = call.get<ProjectService>()
                val response = service.findMany()

                call.respond(HttpStatusCode.OK, response)
            }

            get("/{id}") {
                call.authorize(Role.SUPER)
                val id = call.parameters["id"]?: return@get call.respond(HttpStatusCode.BadRequest)
                val service = call.get<ProjectService>()
                val response = service.findOne(id)?: return@get call.respond(HttpStatusCode.NotFound)

                call.respond(HttpStatusCode.OK, response)
            }

            post("") {
                val fields = call.receive<ProjectCreate>()
                val service = call.get<ProjectService>()
                val response = service.create(fields)

                call.respond(HttpStatusCode.Created, response)
            }

            patch("/{id}") {
                val id = call.parameters["id"]?: return@patch call.respond(HttpStatusCode.BadRequest)
                val fields = call.receive<ProjectUpdate>()
                val service = call.get<ProjectService>()
                val response = service.update(id, fields)

                call.respond(HttpStatusCode.OK, response)
            }

            patch("/{id}/restore") {
                call.authorize(Role.SUPER)
                val id = call.parameters["id"]?: return@patch call.respond(HttpStatusCode.BadRequest)
                val service = call.get<ProjectService>()
                service.restore(id)

                call.respond(HttpStatusCode.NoContent)
            }

            delete("/{id}/soft") {
                val id = call.parameters["id"]?: return@delete call.respond(HttpStatusCode.BadRequest)
                val service = call.get<ProjectService>()
                service.softDelete(id)

                call.respond(HttpStatusCode.NoContent)
            }

            delete("/{id}") {
                call.authorize(Role.SUPER)
                val id = call.parameters["id"]?: return@delete call.respond(HttpStatusCode.BadRequest)
                val service = call.get<ProjectService>()
                service.delete(id)

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}