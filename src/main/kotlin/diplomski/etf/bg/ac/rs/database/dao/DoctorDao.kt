package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.responses.AppointmentForDoctorResponse

interface DoctorDao {

    fun getAppointmentsForDoctor(doctorId: String): List<AppointmentForDoctorResponse>
    fun cancelAppointment(appointmentId: Int, callerRole: Int): Int
}