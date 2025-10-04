package top.dedicado.infrastructure.plugins

import io.ktor.server.application.*
import kotlinx.coroutines.launch
import top.dedicado.infrastructure.EnvironmentConfig

fun Application.configureDatabases() {
    val isProduction = System.getenv("KTOR_DEPLOYMENT_ENVIRONMENT") == "production"
    val environmentConfig = EnvironmentConfig(isProduction)

    if(!environmentConfig.isProduction) {
        monitor.subscribe(ApplicationStarted) {

            launch {}
        }
    }
}
