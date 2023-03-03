package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class ClinicForPatient(
    val id: String,
    val email: String,
    val name: String,
    val openingTime: LocalTime,
    val workHours: Int,
    var doctors: List<DoctorForPatient>
)