package top.dedicado.infrastructure.secrets

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import io.ktor.server.config.ApplicationConfig
import top.dedicado.infrastructure.EnvironmentConfig

object SecretsProvider {

    fun load(applicationConfig: ApplicationConfig, environmentConfig: EnvironmentConfig): SecretKeys {
        return if (environmentConfig.isProduction) {
            loadFromProduction(applicationConfig)
        } else {
            loadFromDevelop(applicationConfig)
        }
    }

    private fun loadFromDevelop(config: ApplicationConfig): SecretKeys {
        return SecretKeys(
            componentUri = config.property("mongodb.componentUri").getString(),
            principalUri = config.property("mongodb.principalUri").getString(),
            jwtSecret = config.property("jwt.secret").getString(),
            awsAccessKey = config.property("aws.accessKey").getString(),
            awsSecretAccessKey = config.property("aws.secretAccessKey").getString(),
        )
    }

    private fun loadFromProduction(config: ApplicationConfig): SecretKeys {
        val projectId = config.property("gcp.projectId").getString()
        SecretManagerServiceClient.create().use { client ->
            return SecretKeys(
                componentUri = client.accessSecret("COMPONENT_URI", projectId),
                principalUri = client.accessSecret("PRINCIPAL_URI", projectId),
                jwtSecret = client.accessSecret("JWT_SECRET", projectId),
                awsAccessKey = client.accessSecret("AWS_ACCESS_KEY", projectId),
                awsSecretAccessKey = client.accessSecret("AWS_SECRET_ACCESS_KEY", projectId),
            )
        }
    }

    private fun SecretManagerServiceClient.accessSecret(secretId: String, projectId: String): String {
        val secretVersionName = SecretVersionName.of(projectId, secretId, "latest")
        return this.accessSecretVersion(secretVersionName).payload.data.toStringUtf8()
    }
}