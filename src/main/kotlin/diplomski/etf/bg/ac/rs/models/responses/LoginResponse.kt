package diplomski.etf.bg.ac.rs.models.responses

import diplomski.etf.bg.ac.rs.models.database_models.User
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val hasEmailError: Boolean = false,
    val hasPasswordError: Boolean = false,
    val hasRoleError: Boolean = false,
    val token: String? = null,
    val user: User? = null
)
