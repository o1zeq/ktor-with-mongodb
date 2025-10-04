package top.dedicado

import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import top.dedicado.infrastructure.plugins.*

fun main(args: Array<String>) {

    dotenv {
        ignoreIfMissing = true
        systemProperties = true
    }

    EngineMain.main(args)
}

fun Application.module() {

    configureFrameworks()
    configureSockets()
    configureDatabases()
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSSE()
    configureStatus()
    configureRouting()
    configureValidations()
}