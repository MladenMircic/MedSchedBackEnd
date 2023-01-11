package diplomski.etf.bg.ac.rs.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class PasswordChangeResponse(
    val oldPasswordCorrect: Boolean = true,
    val passwordUpdateSuccess: Boolean = false
)