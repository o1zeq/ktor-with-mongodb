package top.dedicado.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    PERSONAL,
    PROFESSIONAL,
    ENTERPRISE,
    SUPER
}