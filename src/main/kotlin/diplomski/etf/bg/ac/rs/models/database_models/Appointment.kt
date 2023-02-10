package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class Appointment(
    val id: Int,
    val date: LocalDate,
    val time: LocalTime,
    val doctorId: Int,
    val patientId: Int,
    val examId: Int,
    val confirmed: Boolean,
    val cancelledBy: Int
)
