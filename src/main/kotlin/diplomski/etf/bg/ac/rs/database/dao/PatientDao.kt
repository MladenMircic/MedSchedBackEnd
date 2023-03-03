package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.*
import diplomski.etf.bg.ac.rs.models.requests.AppointmentsRequest
import diplomski.etf.bg.ac.rs.models.requests.InfoChangeRequest
import diplomski.etf.bg.ac.rs.models.responses.AppointmentForPatientResponse
import kotlinx.datetime.LocalTime

// General dao for accessing patient related data
interface PatientDao {

    fun getPatientById(patientId: String): Patient?
    fun getAppointmentsForPatient(patientId: String): List<AppointmentForPatientResponse>
    fun getAppointmentWithDoctorById(appointmentId: Int): AppointmentForPatientResponse?
    fun getAllCategories(): List<Category>
    fun insertService(category: Category): Int
    fun getDoctors(categoryId: Int?): List<DoctorForPatient>
    fun getClinics(categoryId: Int?): List<ClinicForPatient>
    fun getAllAppointmentsForDoctorAtDate(appointmentsRequest: AppointmentsRequest): List<LocalTime>
    fun getAllServicesForDoctor(doctorId: String): List<Service>
    fun scheduleAppointment(appointment: Appointment): Int
    fun cancelAppointment(appointmentId: Int, callerRole: Int): Int
    fun updateEmail(patientId: String, email: String): Int
    fun updatePassword(patientId: String, newPassword: String): Int
    fun updateInfo(patientId: String, infoChangeRequest: InfoChangeRequest): Int
}