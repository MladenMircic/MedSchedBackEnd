package diplomski.etf.bg.ac.rs.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val hasEmailError: Boolean = false,
    val hasPasswordError: Boolean = false,
    val token: String? = null
)