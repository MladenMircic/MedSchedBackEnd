package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class DoctorWorkTime(
    val doctorId: String,
    val clinicId: String,
    val dayOfWeek: DayOfWeek,
    val time: LocalTime
)