package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.PatientDao
import diplomski.etf.bg.ac.rs.database.entities.*
import diplomski.etf.bg.ac.rs.models.database_models.*
import diplomski.etf.bg.ac.rs.models.requests.AppointmentsRequest
import diplomski.etf.bg.ac.rs.models.responses.ScheduledResponse
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime
import org.ktorm.database.Database
import org.ktorm.dsl.*

class PatientDaoImpl(private val database: Database): PatientDao {
    override fun getPatientById(patientId: Int): Patient? =
        database
            .from(PatientEntity)
            .select()
            .where {
                PatientEntity.id eq patientId
            }
            .map {
                Patient(
                    id = patientId,
                    email = it[PatientEntity.email]!!,
                    firstName = it[PatientEntity.first_name]!!,
                    lastName = it[PatientEntity.last_name]!!,
                    password = it[PatientEntity.password]!!,
                    phone = it[PatientEntity.phone]!!,
                    ssn = it[PatientEntity.ssn]!!
                )
            }.firstOrNull()

    override fun getScheduledForPatient(patientId: Int): List<ScheduledResponse> =
        database
            .from(AppointmentEntity)
            .innerJoin(DoctorEntity, on = AppointmentEntity.doctor_id eq DoctorEntity.id)
            .select(
                DoctorEntity.first_name, DoctorEntity.last_name, DoctorEntity.specialization,
                AppointmentEntity.id, AppointmentEntity.date, AppointmentEntity.time,
                AppointmentEntity.doctor_id, AppointmentEntity.patient_id, AppointmentEntity.exam_name
            )
            .where {
                AppointmentEntity.patient_id eq patientId
            }
            .map {
                ScheduledResponse(
                    doctorName = "${it[DoctorEntity.first_name]} ${it[DoctorEntity.last_name]}",
                    doctorSpecialization = it[DoctorEntity.specialization]!!,
                    appointment = Appointment(
                        id = it[AppointmentEntity.id]!!,
                        date = it[AppointmentEntity.date]!!.toKotlinLocalDate(),
                        time = it[AppointmentEntity.time]!!.toKotlinLocalTime(),
                        doctorId = it[AppointmentEntity.doctor_id]!!,
                        patientId = it[AppointmentEntity.patient_id]!!,
                        examName = it[AppointmentEntity.exam_name]!!
                    )
                )
            }

    override fun getAllCategories(): List<Category> =
        database
            .from(CategoryEntity)
            .select()
            .map {
                Category(
                    name = it[CategoryEntity.name]!!
                )
            }

    override fun insertService(category: Category): Int =
        database.insert(CategoryEntity) {
            set(it.name, category.name)
        }

    override fun getDoctors(category: String?): List<DoctorsForPatient> {
        var query = database.from(DoctorEntity).select()
        if (category != null && category != "") {
            println(category)
            query = query.where {
                DoctorEntity.service eq category
            }
        }
        return query.map {
            DoctorsForPatient(
                id = it[DoctorEntity.id]!!,
                email = it[DoctorEntity.email]!!,
                firstName = it[DoctorEntity.first_name]!!,
                lastName = it[DoctorEntity.last_name]!!,
                phone = it[DoctorEntity.phone]!!,
                service = it[DoctorEntity.service]!!,
                specialization = it[DoctorEntity.specialization]!!
            )
        }
    }

    override fun getAllAppointmentsForDoctorAtDate(appointmentsRequest: AppointmentsRequest): List<Appointment> =
        database
            .from(AppointmentEntity)
            .select()
            .where {
                (AppointmentEntity.doctor_id eq appointmentsRequest.doctorId) and
                        (AppointmentEntity.date eq appointmentsRequest.date.toJavaLocalDate())
            }
            .orderBy(AppointmentEntity.time.asc())
            .map {
                Appointment(
                    id = it[AppointmentEntity.id]!!,
                    date = it[AppointmentEntity.date]!!.toKotlinLocalDate(),
                    time = it[AppointmentEntity.time]!!.toKotlinLocalTime(),
                    doctorId = it[AppointmentEntity.doctor_id]!!,
                    patientId = it[AppointmentEntity.patient_id]!!,
                    examName = it[AppointmentEntity.exam_name]!!
                )
            }

    override fun getAllServicesForDoctor(doctorId: Int): List<Service> =
        database
            .from(DoctorEntity)
            .innerJoin(ServiceEntity, on = DoctorEntity.service eq ServiceEntity.category)
            .select(ServiceEntity.id, ServiceEntity.name, ServiceEntity.category)
            .where {
                DoctorEntity.id eq doctorId
            }
            .map {
                Service(
                    id = it[ServiceEntity.id]!!,
                    name = it[ServiceEntity.name]!!,
                    category = it[ServiceEntity.category]!!
                )
            }

    override fun scheduleAppointment(appointment: Appointment): Int =
        database.insert(AppointmentEntity) {
            set(it.id, appointment.id)
            set(it.date, appointment.date.toJavaLocalDate())
            set(it.time, appointment.time.toJavaLocalTime())
            set(it.doctor_id, appointment.doctorId)
            set(it.patient_id, appointment.patientId)
            set(it.exam_name, appointment.examName)
        }

    override fun cancelAppointment(appointmentId: Int): Int =
        database.delete(AppointmentEntity) { AppointmentEntity.id eq appointmentId }

    override fun updateEmail(patientId: Int, email: String): Int =
        database.update(PatientEntity) {
            set(it.email, email)
            where {
                it.id eq patientId
            }
        }

    override fun updatePassword(patientId: Int, newPassword: String): Int =
        database.update(PatientEntity) {
            set(it.password, newPassword)
            where {
                it.id eq patientId
            }
        }
}