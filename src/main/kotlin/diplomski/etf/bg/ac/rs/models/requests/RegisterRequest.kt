package diplomski.etf.bg.ac.rs.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val role: Int,
    val phone: String,
    val ssn: String
)
