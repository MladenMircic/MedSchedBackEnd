package diplomski.etf.bg.ac.rs.models.requests

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentsRequest(
    val doctorId: String,
    val date: LocalDate
)