package top.dedicado.presentation.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val message: String,
    val idToken: String,
    val refreshIdToken: String,
)
