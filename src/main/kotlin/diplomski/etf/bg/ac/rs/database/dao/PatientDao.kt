package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.*
import diplomski.etf.bg.ac.rs.models.requests.AppointmentsRequest
import diplomski.etf.bg.ac.rs.models.requests.InfoChangeRequest
import diplomski.etf.bg.ac.rs.models.responses.ScheduledResponse
import kotlinx.datetime.LocalDate
import java.time.LocalDateTime

// General dao for accessing patient related data
interface PatientDao {

    fun getPatientById(patientId: Int): Patient?
    fun getScheduledForPatient(patientId: Int): List<ScheduledResponse>
    fun getAllCategories(): List<Category>
    fun insertService(category: Category): Int
    fun getDoctors(category: String?): List<DoctorsForPatient>
    fun getAllAppointmentsForDoctorAtDate(appointmentsRequest: AppointmentsRequest): List<Appointment>
    fun getAllServicesForDoctor(doctorId: Int): List<Service>
    fun scheduleAppointment(appointment: Appointment): Int
    fun cancelAppointment(appointmentId: Int): Int
    fun updateEmail(patientId: Int, email: String): Int
    fun updatePassword(patientId: Int, newPassword: String): Int
    fun updateInfo(patientId: Int, infoChangeRequest: InfoChangeRequest): Int
}