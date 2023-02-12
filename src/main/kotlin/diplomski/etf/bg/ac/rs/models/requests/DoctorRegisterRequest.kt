package diplomski.etf.bg.ac.rs.models.requests

import diplomski.etf.bg.ac.rs.models.WorkDay
import kotlinx.serialization.Serializable

@Serializable
data class DoctorRegisterRequest(
    val email: String,
    var password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val categoryId: Int,
    val specializationId: Int,
    val workDays: List<WorkDay>
)