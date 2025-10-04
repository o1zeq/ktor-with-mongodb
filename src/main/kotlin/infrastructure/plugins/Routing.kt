package top.dedicado.infrastructure.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import top.dedicado.presentation.routes.authRoute
import top.dedicado.presentation.routes.component.activityRoute
import top.dedicado.presentation.routes.fileRoute
import top.dedicado.presentation.routes.principal.contactRoute
import top.dedicado.presentation.routes.principal.membershipRoute
import top.dedicado.presentation.routes.principal.projectRoute
import top.dedicado.presentation.routes.principal.sessionRoute
import top.dedicado.presentation.routes.principal.subscriptionRoute
import top.dedicado.presentation.routes.principal.accountRoute
import top.dedicado.presentation.routes.registerRoute

fun Application.configureRouting() {

    routing {
        route("") {
            get("") {
                call.respond(HttpStatusCode.NoContent)
            }
            get("/_ah/warmup") {
                application.log.info("Warmup request received. Initializing application...")
                call.respond(HttpStatusCode.OK)
            }
            activityRoute()

            accountRoute()
            contactRoute()
            membershipRoute()
            projectRoute()
            sessionRoute()
            subscriptionRoute()

            authRoute()
            fileRoute()
            registerRoute()
        }
    }
}
