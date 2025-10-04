package top.dedicado.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(
    val latitude: Double,
    val longitude: Double
)
