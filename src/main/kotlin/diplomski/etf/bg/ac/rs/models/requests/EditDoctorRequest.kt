package diplomski.etf.bg.ac.rs.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class EditDoctorRequest (
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String
)