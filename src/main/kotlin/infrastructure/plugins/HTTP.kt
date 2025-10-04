package top.dedicado.infrastructure.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.ktor.ext.inject
import top.dedicado.infrastructure.EnvironmentConfig

fun Application.configureHTTP() {
    val environmentConfig by inject<EnvironmentConfig>()

    install(CORS) {
        allowCredentials = true

        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)

        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader("Dedicado")

        if (environmentConfig.isProduction) {
            allowHost("dedicado.top", schemes = listOf("https"))

            allowHost("suite.dedicado.top", schemes = listOf("https"))

            allowHost("suite-dedicado.web.app", schemes = listOf("https"))

            allowHost("suite-dedicado.firebaseapp.com", schemes = listOf("https"))
        } else {
            anyHost()
        }
    }
}
