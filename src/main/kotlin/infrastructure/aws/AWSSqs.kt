package top.dedicado.infrastructure.aws

import aws.sdk.kotlin.services.sqs.SqsClient
import aws.sdk.kotlin.services.sqs.model.SendMessageRequest
import io.ktor.server.config.ApplicationConfig
import top.dedicado.infrastructure.secrets.SecretKeys

class AWSSqs(
    private val applicationConfig: ApplicationConfig,
    private val secretKeys: SecretKeys,
): AWSCredentials(applicationConfig, secretKeys) {

    private val sqsClient = SqsClient {
        credentialsProvider = this@AWSSqs.credentialsProvider
        region = this@AWSSqs.region
    }

    suspend fun send(input: SendMessageRequest) = this.sqsClient.sendMessage(input)
}