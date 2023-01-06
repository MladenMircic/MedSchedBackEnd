package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.*
import diplomski.etf.bg.ac.rs.models.requests.AppointmentsRequest
import kotlinx.datetime.LocalDate
import java.time.LocalDateTime

// General dao for accessing patient related data
interface PatientDao {

    fun getScheduledForPatient(user: User)
    fun getAllCategories(): List<Category>
    fun insertService(category: Category): Int
    fun getDoctors(category: String?): List<DoctorsForPatient>
    fun getAllAppointmentsForDoctorAtDate(appointmentsRequest: AppointmentsRequest): List<Appointment>
    fun getAllServicesForDoctor(doctorId: Int): List<Service>
    fun scheduleAppointment(appointment: Appointment): Int
}