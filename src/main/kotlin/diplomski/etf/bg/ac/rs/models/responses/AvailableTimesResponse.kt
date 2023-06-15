package diplomski.etf.bg.ac.rs.models.responses

import diplomski.etf.bg.ac.rs.models.database_models.Appointment
import diplomski.etf.bg.ac.rs.models.database_models.ClinicForPatient
import diplomski.etf.bg.ac.rs.models.database_models.DoctorWorkTime
import kotlinx.serialization.Serializable

@Serializable
data class AvailableTimesResponse(
    val doctorWorkTimes: List<DoctorWorkTime>,
    val scheduledAppointments: List<Appointment>,
    val clinicsList: List<ClinicForPatient>
)