package top.dedicado.infrastructure.aws

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials
import io.ktor.server.config.ApplicationConfig
import org.koin.core.component.KoinComponent
import top.dedicado.infrastructure.secrets.SecretKeys

abstract class AWSCredentials(
    applicationConfig: ApplicationConfig,
    secretKeys: SecretKeys
): KoinComponent {
    protected val region: String = applicationConfig.property("aws.region").getString()
    protected val credentialsProvider: StaticCredentialsProvider

    init {
        val credentials = Credentials(
            secretKeys.awsAccessKey,
            secretKeys.awsSecretAccessKey,
        )
        credentialsProvider = StaticCredentialsProvider(credentials)
    }
}