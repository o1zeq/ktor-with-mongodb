package top.dedicado.infrastructure.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun ApplicationCall.getAccountIdFromPrincipal(): String {
    val principal = principal<JWTPrincipal>()
        ?: throw UnauthorizedException("Token não encontrado ou inválido")
    return principal.payload.getClaim("aid").asString()
}

fun ApplicationCall.getIssuerFromPrincipal(): String {
    val principal = principal<JWTPrincipal>()
        ?: throw UnauthorizedException("Token não encontrado ou inválido")
    return principal.payload.issuer
}

fun ApplicationCall.getRealmFromPrincipal(): String {
    val principal = principal<JWTPrincipal>()
        ?: throw UnauthorizedException("Token não encontrado ou inválido")
    return principal.payload.getClaim("realm").asString()
}
