package top.dedicado.infrastructure.di.modules

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import io.ktor.server.config.ApplicationConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.slf4j.LoggerFactory
import top.dedicado.infrastructure.firebase.FirebaseConstants.FIREBASE_APP
import top.dedicado.infrastructure.firebase.FirebaseConstants.FIREBASE_AUTH
import java.io.FileInputStream

val firebaseModule = module {

    single<FirebaseApp>(createdAtStart = true, qualifier = named(FIREBASE_APP)) {
        val applicationConfig = get<ApplicationConfig>()
        val loggerFactory = LoggerFactory.getLogger("FirebaseModule")

        val projectId = applicationConfig.propertyOrNull("gcp.projectId")?.getString()

        val pathToCredentials = applicationConfig.propertyOrNull("firebase.credentials")?.getString() ?: ""
        val isProduction = applicationConfig.propertyOrNull("ktor.deployment.environment")?.getString() == "production"

        val credentials = if (!isProduction && pathToCredentials.isNotEmpty()) {
            loggerFactory.info("Ambiente de desenvolvimento detectado. Carregando credenciais do arquivo: $pathToCredentials")
            GoogleCredentials.fromStream(FileInputStream(pathToCredentials))
        } else {
            loggerFactory.info("Ambiente de produção ou caminho de credenciais não fornecido. Usando Application Default Credentials.")
            GoogleCredentials.getApplicationDefault()
        }

        val optionsBuilder = FirebaseOptions.builder()
            .setCredentials(credentials)

        if (projectId != null) {
            optionsBuilder.setProjectId(projectId)
        }

        val options = optionsBuilder.build()

        if (FirebaseApp.getApps().none { it.name == FIREBASE_APP }) {
            FirebaseApp.initializeApp(options, FIREBASE_APP)
        } else {
            FirebaseApp.getInstance(FIREBASE_APP)
        }
    }

    single<FirebaseAuth>(named(FIREBASE_AUTH)) {
        FirebaseAuth.getInstance(get(named(FIREBASE_APP)))
    }
}