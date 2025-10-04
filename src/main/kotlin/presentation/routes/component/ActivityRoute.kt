package top.dedicado.presentation.routes.component

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.get
import top.dedicado.domain.models.Role
import top.dedicado.domain.services.component.ActivityService
import top.dedicado.infrastructure.plugins.authorize
import top.dedicado.presentation.dto.component.activity.ActivityCreate

fun Route.activityRoute() {
    route("activities") {
        authenticate("jwt") {
            get("") {
                call.authorize(Role.SUPER)
                val service = call.get<ActivityService>()
                val response = service.findMany()

                call.respond(HttpStatusCode.OK,response)
            }

            get("/{id}") {
                call.authorize(Role.SUPER)
                val id = call.parameters["id"]?: return@get call.respond(HttpStatusCode.BadRequest)
                val service = call.get<ActivityService>()
                val response = service.findById(id)?: return@get call.respond(HttpStatusCode.NotFound)

                call.respond(HttpStatusCode.OK,response)
            }

            post("") {
                val request = call.receive<ActivityCreate>()
                val service = call.get<ActivityService>()
                val response = service.create(request)

                call.respond(HttpStatusCode.Created,response)
            }

            delete("/{id}") {
                call.authorize(Role.SUPER)
                val id = call.parameters["id"]?: return@delete call.respond(HttpStatusCode.BadRequest)
                val service = call.get<ActivityService>()
                service.delete(id)

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}