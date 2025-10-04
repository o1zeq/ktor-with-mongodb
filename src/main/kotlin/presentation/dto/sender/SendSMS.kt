package top.dedicado.presentation.dto.sender

import kotlinx.serialization.Serializable

@Serializable
data class SendSMS(
    val to: String,
    val content: String
)