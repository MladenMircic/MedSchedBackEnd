package diplomski.etf.bg.ac.rs.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val success: Boolean = false,
    val accountExists: Boolean = false
)
