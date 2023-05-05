package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.DatabaseConnection
import diplomski.etf.bg.ac.rs.database.dao.DoctorDao
import diplomski.etf.bg.ac.rs.database.entities.*
import diplomski.etf.bg.ac.rs.models.database_models.Appointment
import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.database_models.Service
import diplomski.etf.bg.ac.rs.models.responses.AppointmentForDoctorResponse
import diplomski.etf.bg.ac.rs.utils.Role
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime
import org.ktorm.database.Database
import org.ktorm.dsl.*

class DoctorDaoImpl(private val database: Database): DoctorDao {
    override fun getDoctorNameById(doctorId: String): String? =
        database
            .from(DoctorEntity)
            .select(DoctorEntity.first_name, DoctorEntity.last_name)
            .where {
                DoctorEntity.id eq doctorId
            }
            .map {
                "${it[DoctorEntity.first_name]!!} ${it[DoctorEntity.last_name]!!}"
            }.firstOrNull()

    override fun getAppointmentsForDoctor(doctorId: String): List<AppointmentForDoctorResponse> =
        database
            .from(AppointmentEntity)
            .innerJoin(PatientEntity, on = AppointmentEntity.patient_id eq PatientEntity.id)
            .select(
                PatientEntity.first_name, PatientEntity.last_name,
                AppointmentEntity.id, AppointmentEntity.date, AppointmentEntity.time,
                AppointmentEntity.doctor_id, AppointmentEntity.patient_id,
                AppointmentEntity.confirmed, AppointmentEntity.cancelled_by
            )
            .where {
                AppointmentEntity.doctor_id eq doctorId and ( AppointmentEntity.cancelled_by neq Role.DOCTOR.ordinal )
            }
            .map {
                val appointmentId = it[AppointmentEntity.id]!!
                AppointmentForDoctorResponse(
                    patientName = "${it[PatientEntity.first_name]} ${it[PatientEntity.last_name]}",
                    appointment = Appointment(
                        id = appointmentId,
                        date = it[AppointmentEntity.date]!!.toKotlinLocalDate(),
                        time = it[AppointmentEntity.time]!!.toKotlinLocalTime(),
                        doctorId = it[AppointmentEntity.doctor_id]!!,
                        patientId = it[AppointmentEntity.patient_id]!!,
                        services = getServicesForAppointment(appointmentId),
                        confirmed = it[AppointmentEntity.confirmed]!!,
                        cancelledBy = it[AppointmentEntity.cancelled_by]!!
                    )
                )
            }

    override fun cancelAppointment(appointmentId: Int, callerRole: Int): Appointment? {
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
                    services = getServicesForAppointment(appointmentId),
                    confirmed = it[AppointmentEntity.confirmed]!!,
                    cancelledBy = it[AppointmentEntity.cancelled_by]!!
                )
            }.firstOrNull()
        if (appointment != null) {
            database.update(AppointmentEntity) {
                set(it.confirmed, false)
                set(it.cancelled_by, callerRole)
                where { it.id eq appointmentId }
            }
        }
        return appointment
    }

    override fun dismissAppointment(appointmentId: Int): Boolean =
        database.delete(AppointmentServiceEntity) { AppointmentServiceEntity.appointment_id eq appointmentId } > 0 &&
        database.delete(AppointmentEntity) { AppointmentEntity.id eq appointmentId } > 0

    private fun getServicesForAppointment(appointmentId: Int): List<Service> =
        DatabaseConnection.database
            .from(AppointmentServiceEntity)
            .innerJoin(ServiceEntity, on = AppointmentServiceEntity.service_id eq ServiceEntity.id)
            .select()
            .where { AppointmentServiceEntity.appointment_id eq appointmentId }
            .map {row ->
                Service(
                    id = row[ServiceEntity.id]!!,
                    name = row[ServiceEntity.name]!!,
                    categoryId = row[ServiceEntity.category_id]!!
                )
            }
}