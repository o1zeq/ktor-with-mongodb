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
import top.dedicado.domain.services.principal.AccountService
import top.dedicado.infrastructure.plugins.authorize
import top.dedicado.infrastructure.plugins.getAccountIdFromPrincipal
import top.dedicado.presentation.dto.ResetPassword
import top.dedicado.presentation.dto.UpdatePassword
import top.dedicado.presentation.dto.principal.account.AccountCreate
import top.dedicado.presentation.dto.principal.account.AccountUpdate

fun Route.accountRoute() {
    route("/accounts") {
        post("/reset-password") {
            val fields = call.receive<ResetPassword>()
            val service = call.get<AccountService>()
            val response = service.resetPassword(fields)

            call.respond(HttpStatusCode.Created, mapOf("message" to response))
        }

        authenticate("jwt") {
            get("/many") {
                call.authorize(Role.SUPER)
                val service = call.get<AccountService>()
                val response = service.findMany()

                call.respond(HttpStatusCode.OK, response)
            }

            get("/one/{id}") {
                call.authorize(Role.SUPER)
                val id = call.parameters["id"]?: return@get call.respond(HttpStatusCode.BadRequest)
                val service = call.get<AccountService>()
                val response = service.findOne(id)?: return@get call.respond(HttpStatusCode.NotFound)

                call.respond(HttpStatusCode.OK, response)
            }

            get("") {
                val accountId = call.getAccountIdFromPrincipal()
                val service = call.get<AccountService>()
                val response = service.findMe(accountId)

                call.respond(HttpStatusCode.OK, response)
            }

            post("") {
                call.authorize(Role.SUPER)
                val fields = call.receive<AccountCreate>()
                val service = call.get<AccountService>()
                val response = service.create(fields)

                call.respond(HttpStatusCode.Created, response)
            }

            patch("") {
                val accountId = call.getAccountIdFromPrincipal()
                val fields = call.receive<AccountUpdate>()
                val service = call.get<AccountService>()
                val response = service.update(accountId, fields)

                call.respond(HttpStatusCode.OK, response)
            }

            patch("/update-password") {
                val accountId = call.getAccountIdFromPrincipal()
                val fields = call.receive<UpdatePassword>()
                val service = call.get<AccountService>()
                val response = service.updatePassword(accountId, fields)

                call.respond(HttpStatusCode.OK, mapOf("message" to response))
            }

            patch("/{id}") {
                call.authorize(Role.SUPER)
                val id = call.parameters["id"]?: return@patch call.respond(HttpStatusCode.BadRequest)
                val fields = call.receive<AccountUpdate>()
                val service = call.get<AccountService>()
                val response = service.update(id, fields)

                call.respond(HttpStatusCode.OK, response)
            }

            patch("/{id}/restore") {
                call.authorize(Role.SUPER)
                val id = call.parameters["id"]?: return@patch call.respond(HttpStatusCode.BadRequest)
                val service = call.get<AccountService>()
                service.restore(id)

                call.respond(HttpStatusCode.NoContent)
            }

            delete("/{id}/soft") {
                call.authorize(Role.SUPER)
                val id = call.parameters["id"]?: return@delete call.respond(HttpStatusCode.BadRequest)
                val service = call.get<AccountService>()
                service.softDelete(id)

                call.respond(HttpStatusCode.NoContent)
            }

            delete("/{id}") {
                call.authorize(Role.SUPER)
                val id = call.parameters["id"]?: return@delete call.respond(HttpStatusCode.BadRequest)
                val service = call.get<AccountService>()
                service.delete(id)

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}