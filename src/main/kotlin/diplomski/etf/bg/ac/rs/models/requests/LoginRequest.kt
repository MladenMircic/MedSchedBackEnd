package diplomski.etf.bg.ac.rs.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val role: Int
)