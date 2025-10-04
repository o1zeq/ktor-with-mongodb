package top.dedicado.infrastructure.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import top.dedicado.domain.models.Role

fun ApplicationCall.authorize(vararg allowedRoles: Role) {
    val principal = principal<JWTPrincipal>()
        ?: throw ForbiddenException("Você não tem permissão para utilizar esse recurso.")

    val roleString = principal.payload.getClaim("role").asString()
    val role = Role.valueOf(roleString)

    if (role !in allowedRoles) {
        throw ForbiddenException("Você não tem permissão para executar esta ação.")
    }
}