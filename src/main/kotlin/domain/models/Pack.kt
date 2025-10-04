package top.dedicado.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class Pack {
    PERSONAL,
    EXECUTIVE,
    ENTERPRISE,
    CORPORATE
}