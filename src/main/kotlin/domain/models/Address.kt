package top.dedicado.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val kind: String? = null,
    val zipcode: String? = null,
    val street: String? = null,
    val number: String? = null,
    val complement: String? = null,
    val district: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val coordinate: Coordinate? = null
)
