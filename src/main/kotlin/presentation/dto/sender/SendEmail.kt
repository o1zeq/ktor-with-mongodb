package top.dedicado.presentation.dto.sender

import kotlinx.serialization.Serializable
import top.dedicado.utils.EmailTemplate

@Serializable
data class SendEmail(
    val to: String,
    val bcc: List<String> = emptyList(),
    val subject: String,
    val template: EmailTemplate,
    val attributes: Map<String, String>
)