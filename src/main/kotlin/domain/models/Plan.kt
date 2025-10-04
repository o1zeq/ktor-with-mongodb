package top.dedicado.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class Plan {
    PERSONAL,PROFESSIONAL,BUSINESS,ENTERPRISE
}