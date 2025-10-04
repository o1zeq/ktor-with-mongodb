package top.dedicado.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class Attribute {
    GUEST,OBSERVER,TEAM,ADMIN,OWNER,SUPER
}