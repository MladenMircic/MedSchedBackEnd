package diplomski.etf.bg.ac.rs.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class EmailChangeRequest(
    val email: String
)