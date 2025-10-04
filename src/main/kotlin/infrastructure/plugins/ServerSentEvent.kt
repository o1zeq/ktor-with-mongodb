package top.dedicado.infrastructure.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sse.*

fun Application.configureSSE() {
    install(SSE)
}