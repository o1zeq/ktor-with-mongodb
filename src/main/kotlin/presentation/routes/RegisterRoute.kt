package top.dedicado.presentation.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.get
import top.dedicado.domain.services.RegisterService
import top.dedicado.presentation.dto.register.RegisterRequest

fun Route.registerRoute() {
    route("/register") {
        post("") {
            val request = call.receive<RegisterRequest>()
            val service = call.get<RegisterService>()
            val response = service.insert(request)

            call.respond(HttpStatusCode.Created, mapOf("message" to response ))
        }
    }
}