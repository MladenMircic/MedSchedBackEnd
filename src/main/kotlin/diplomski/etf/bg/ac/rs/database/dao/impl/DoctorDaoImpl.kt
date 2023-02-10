package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.DoctorDao
import diplomski.etf.bg.ac.rs.database.entities.AppointmentEntity
import diplomski.etf.bg.ac.rs.database.entities.PatientEntity
import diplomski.etf.bg.ac.rs.models.database_models.Appointment
import diplomski.etf.bg.ac.rs.models.responses.AppointmentForDoctorResponse
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime
import org.ktorm.database.Database
import org.ktorm.dsl.*

class DoctorDaoImpl(private val database: Database): DoctorDao {

    override fun getAppointmentsForDoctor(doctorId: Int): List<AppointmentForDoctorResponse> =
        database
            .from(AppointmentEntity)
            .innerJoin(PatientEntity, on = AppointmentEntity.patient_id eq PatientEntity.id)
            .select(
                PatientEntity.first_name, PatientEntity.last_name,
                AppointmentEntity.id, AppointmentEntity.date, AppointmentEntity.time,
                AppointmentEntity.doctor_id, AppointmentEntity.patient_id, AppointmentEntity.exam_id,
                AppointmentEntity.confirmed
            )
            .where {
                AppointmentEntity.doctor_id eq doctorId
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
                        confirmed = it[AppointmentEntity.confirmed]!!
                    )
                )
            }
}