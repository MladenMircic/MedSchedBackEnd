package diplomski.etf.bg.ac.rs.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class PasswordChangeRequest(
    val oldPassword: String,
    val newPassword: String
)