package diplomski.etf.bg.ac.rs.models.responses

import diplomski.etf.bg.ac.rs.models.database_models.Appointment
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentForDoctorResponse(
    val patientName: String,
    val appointment: Appointment
)