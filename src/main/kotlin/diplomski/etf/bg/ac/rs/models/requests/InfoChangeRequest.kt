package diplomski.etf.bg.ac.rs.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class InfoChangeRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val ssn: String
)