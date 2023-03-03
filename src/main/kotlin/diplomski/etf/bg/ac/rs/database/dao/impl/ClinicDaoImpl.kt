package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.ClinicDao
import diplomski.etf.bg.ac.rs.database.entities.CategoryEntity
import diplomski.etf.bg.ac.rs.database.entities.DoctorEntity
import diplomski.etf.bg.ac.rs.database.entities.DoctorWorkTimeEntity
import diplomski.etf.bg.ac.rs.database.entities.ServiceEntity
import diplomski.etf.bg.ac.rs.models.WorkDay
import diplomski.etf.bg.ac.rs.models.database_models.Category
import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.database_models.Service
import diplomski.etf.bg.ac.rs.models.requests.DoctorRegisterRequest
import kotlinx.datetime.toJavaLocalTime
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.util.*

class ClinicDaoImpl(private val database: Database): ClinicDao {
    override fun getAllDoctors(): List<Doctor> =
        database
            .from(DoctorEntity)
            .select()
            .map {
                Doctor(
                    id = it[DoctorEntity.id]!!,
                    email = it[DoctorEntity.email]!!,
                    firstName = it[DoctorEntity.first_name]!!,
                    lastName = it[DoctorEntity.last_name]!!,
                    password = "",
                    phone = it[DoctorEntity.last_name]!!,
                    categoryId = it[DoctorEntity.category_id]!!,
                    specializationId = it[DoctorEntity.specialization_id]!!
                )
            }

    override fun getAllCategories(): List<Category> =
        database
            .from(CategoryEntity)
            .select()
            .map {
                Category(
                    id = it[CategoryEntity.id]!!,
                    name = it[CategoryEntity.name]!!
                )
            }

    override fun getAllServicesForCategory(categoryId: Int): List<Service> =
        database
            .from(ServiceEntity)
            .select()
            .where {
                ServiceEntity.category_id eq categoryId
            }
            .map {
                Service(
                    id = it[ServiceEntity.id]!!,
                    name = it[ServiceEntity.name]!!,
                    categoryId = categoryId
                )
            }

    override fun registerDoctor(doctorRegisterRequest: DoctorRegisterRequest, clinicId: String): Boolean {
        val doctorId: String = UUID.randomUUID().toString()
        database.insert(DoctorEntity) {
            set(it.id, doctorId)
            set(it.email, doctorRegisterRequest.email)
            set(it.first_name, doctorRegisterRequest.firstName)
            set(it.last_name, doctorRegisterRequest.lastName)
            set(it.password, doctorRegisterRequest.password)
            set(it.phone, doctorRegisterRequest.phone)
            set(it.category_id, doctorRegisterRequest.categoryId)
            set(it.specialization_id, doctorRegisterRequest.specializationId)
        }
        return registerDoctorWorkDays(doctorId, clinicId, doctorRegisterRequest.workDays)
    }

    override fun deleteDoctor(doctorId: String): Int {
        if (doctorId == "") return 0
        val doctorDeleteResult = database.delete(DoctorEntity) { it.id eq doctorId }
        val workTimeDeleteResult = database.delete(DoctorWorkTimeEntity) { it.doctor_id eq doctorId }
        return doctorDeleteResult + workTimeDeleteResult
    }

    private fun registerDoctorWorkDays(doctorId: String, clinicId: String, workDays: List<WorkDay>): Boolean {
        return database.batchInsert(DoctorWorkTimeEntity) {
            for (workDay in workDays) {
                for (workHour in workDay.workHours) {
                    item {
                        set(it.id, 0)
                        set(it.doctor_id, doctorId)
                        set(it.clinic_id, clinicId)
                        set(it.day_of_week, workDay.dayOfWeek.value)
                        set(it.time, workHour.toJavaLocalTime())
                    }
                }
            }
        }.all { it != 0 }
    }
}