package top.dedicado.infrastructure.plugins

import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import top.dedicado.infrastructure.EnvironmentConfig
import top.dedicado.infrastructure.di.modules.*

fun Application.configureFrameworks() {
    val isProduction = System.getenv("KTOR_DEPLOYMENT_ENVIRONMENT") == "production"
    val environmentConfig = EnvironmentConfig(isProduction)

    install(Koin) {
        slf4jLogger()
        modules(
            configModule(environment.config, environmentConfig),
            createAppModule()
        )
    }
}

private fun configModule(
    applicationConfig: ApplicationConfig,
    environmentConfig: EnvironmentConfig
) = module {
    single { applicationConfig }
    single { environmentConfig }
}

private fun createAppModule() = module {
    includes(
        awsModule,
        collectionModule,
        databaseModule,
        firebaseModule,
        httpModule,
        indexModule,
        jwtModule,
        repositoryModule,
        secretModule,
        serviceModule,
        storageModule,
    )
}