package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.serialization.Serializable

@Serializable
data class DoctorsForPatient(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val serviceId: Int,
    val specializationId: Int
)