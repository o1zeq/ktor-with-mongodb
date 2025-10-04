package top.dedicado.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val accuracy: Float?,
    val bearing: Float?,
    val speed: Float?,
    val altitude: Double?,
    val coordinate: Coordinate
)
