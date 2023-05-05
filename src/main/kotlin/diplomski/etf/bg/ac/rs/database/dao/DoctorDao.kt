package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.Appointment
import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.responses.AppointmentForDoctorResponse

interface DoctorDao {

    fun getDoctorNameById(doctorId: String): String?
    fun getAppointmentsForDoctor(doctorId: String): List<AppointmentForDoctorResponse>
    fun cancelAppointment(appointmentId: Int, callerRole: Int): Appointment?
    fun dismissAppointment(appointmentId: Int): Boolean
}