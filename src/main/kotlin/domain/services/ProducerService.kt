package top.dedicado.domain.services

import aws.sdk.kotlin.services.sqs.model.SendMessageRequest
import io.ktor.server.config.ApplicationConfig
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import top.dedicado.infrastructure.aws.AWSSqs
import top.dedicado.presentation.dto.sender.SendEmail
import top.dedicado.presentation.dto.sender.SendSMS

class ProducerService(
    private val applicationConfig: ApplicationConfig,
    private val awsSws: AWSSqs
): KoinComponent {
    private val region = this.applicationConfig.property("aws.region").getString()

    private val emailQueueUrl = "https://sqs.${region}.amazonaws.com/116304767987/SendEmail"
    private val smsQueueUrl = "https://sqs.${region}.amazonaws.com/116304767987/SendSMS"

    suspend fun sendEmail(request: SendEmail) {

        this.awsSws.send(SendMessageRequest.Companion {
            queueUrl = emailQueueUrl
            messageBody = Json.encodeToString(SendEmail.serializer(), request)
            delaySeconds = 2
        })
    }

    suspend fun sendSMS(request: SendSMS) {
        this.awsSws.send(SendMessageRequest.Companion {
            queueUrl = smsQueueUrl
            messageBody = Json.encodeToString(SendSMS.serializer(), request)
            delaySeconds = 2
        })
    }
}