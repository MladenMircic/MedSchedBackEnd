package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.PatientDao
import diplomski.etf.bg.ac.rs.database.entities.AppointmentEntity
import diplomski.etf.bg.ac.rs.database.entities.CategoryEntity
import diplomski.etf.bg.ac.rs.database.entities.DoctorEntity
import diplomski.etf.bg.ac.rs.database.entities.ServiceEntity
import diplomski.etf.bg.ac.rs.models.database_models.*
import diplomski.etf.bg.ac.rs.models.requests.AppointmentsRequest
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime
import org.ktorm.database.Database
import org.ktorm.dsl.*

class PatientDaoImpl(private val database: Database): PatientDao {

    override fun getScheduledForPatient(user: User) {
        // TODO implement this
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
}