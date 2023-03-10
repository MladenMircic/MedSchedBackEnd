package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.DoctorDao
import diplomski.etf.bg.ac.rs.database.entities.AppointmentEntity
import diplomski.etf.bg.ac.rs.database.entities.PatientEntity
import diplomski.etf.bg.ac.rs.models.database_models.Appointment
import diplomski.etf.bg.ac.rs.models.responses.AppointmentForDoctorResponse
import diplomski.etf.bg.ac.rs.utils.Role
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime
import org.ktorm.database.Database
import org.ktorm.dsl.*

class DoctorDaoImpl(private val database: Database): DoctorDao {

    override fun getAppointmentsForDoctor(doctorId: String): List<AppointmentForDoctorResponse> =
        database
            .from(AppointmentEntity)
            .innerJoin(PatientEntity, on = AppointmentEntity.patient_id eq PatientEntity.id)
            .select(
                PatientEntity.first_name, PatientEntity.last_name,
                AppointmentEntity.id, AppointmentEntity.date, AppointmentEntity.time,
                AppointmentEntity.doctor_id, AppointmentEntity.patient_id, AppointmentEntity.exam_id,
                AppointmentEntity.confirmed, AppointmentEntity.cancelled_by
            )
            .where {
                AppointmentEntity.doctor_id eq doctorId and ( AppointmentEntity.cancelled_by neq Role.DOCTOR.ordinal )
            }
            .map {
                AppointmentForDoctorResponse(
                    patientName = "${it[PatientEntity.first_name]} ${it[PatientEntity.last_name]}",
                    appointment = Appointment(
                        id = it[AppointmentEntity.id]!!,
                        date = it[AppointmentEntity.date]!!.toKotlinLocalDate(),
                        time = it[AppointmentEntity.time]!!.toKotlinLocalTime(),
                        doctorId = it[AppointmentEntity.doctor_id]!!,
                        patientId = it[AppointmentEntity.patient_id]!!,
                        examId = it[AppointmentEntity.exam_id]!!,
                        confirmed = it[AppointmentEntity.confirmed]!!,
                        cancelledBy = it[AppointmentEntity.cancelled_by]!!
                    )
                )
            }

    override fun cancelAppointment(appointmentId: Int, callerRole: Int): Int {
        val appointment = database
            .from(AppointmentEntity)
            .select()
            .where {
                AppointmentEntity.id eq appointmentId
            }
            .map {
                Appointment(
                    id = it[AppointmentEntity.id]!!,
                    date = it[AppointmentEntity.date]!!.toKotlinLocalDate(),
                    time = it[AppointmentEntity.time]!!.toKotlinLocalTime(),
                    doctorId = it[AppointmentEntity.doctor_id]!!,
                    patientId = it[AppointmentEntity.patient_id]!!,
                    examId = it[AppointmentEntity.exam_id]!!,
                    confirmed = it[AppointmentEntity.confirmed]!!,
                    cancelledBy = it[AppointmentEntity.cancelled_by]!!
                )
            }.firstOrNull()
        return if (appointment == null || !appointment.confirmed && appointment.cancelledBy == callerRole) {
            0
        } else {
            if (appointment.confirmed) {
                database.update(AppointmentEntity) {
                    set(it.confirmed, false)
                    set(it.cancelled_by, callerRole)
                    where { it.id eq appointmentId }
                }
            } else {
                database.delete(AppointmentEntity) { AppointmentEntity.id eq appointmentId }
            }
        }
    }
}