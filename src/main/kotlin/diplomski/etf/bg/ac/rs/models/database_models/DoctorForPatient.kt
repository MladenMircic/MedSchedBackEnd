package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.serialization.Serializable

@Serializable
data class DoctorForPatient(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val serviceId: Int,
    val specializationId: Int
)