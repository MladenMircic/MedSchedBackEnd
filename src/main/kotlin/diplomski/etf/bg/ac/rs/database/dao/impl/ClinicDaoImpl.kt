package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.ClinicDao
import diplomski.etf.bg.ac.rs.database.entities.*
import diplomski.etf.bg.ac.rs.models.WorkDay
import diplomski.etf.bg.ac.rs.models.database_models.Category
import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.database_models.Service
import diplomski.etf.bg.ac.rs.models.requests.DoctorRegisterRequest
import diplomski.etf.bg.ac.rs.models.requests.EditDoctorRequest
import kotlinx.datetime.toJavaLocalTime
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.util.*

class ClinicDaoImpl(private val database: Database): ClinicDao {
    override fun getAllDoctorsForClinic(clinicId: String): List<Doctor> =
        database
            .from(DoctorEntity)
            .innerJoin(DoctorClinicEntity, on = DoctorEntity.id eq DoctorClinicEntity.doctor_id)
            .select()
            .where {
                DoctorClinicEntity.clinic_id eq clinicId
            }
            .map {
                Doctor(
                    id = it[DoctorEntity.id]!!,
                    email = it[DoctorEntity.email]!!,
                    firstName = it[DoctorEntity.first_name]!!,
                    lastName = it[DoctorEntity.last_name]!!,
                    password = "",
                    phone = it[DoctorEntity.phone]!!,
                    categoryId = it[DoctorEntity.category_id]!!,
                    specializationId = it[DoctorEntity.specialization_id]!!
                )
            }

    override fun getDoctorByEmail(email: String): Doctor? =
        database
            .from(DoctorEntity)
            .select()
            .where {
                DoctorEntity.email eq email
            }
            .map {
                Doctor(
                    id = it[DoctorEntity.id]!!,
                    email = it[DoctorEntity.email]!!,
                    firstName = it[DoctorEntity.first_name]!!,
                    lastName = it[DoctorEntity.last_name]!!,
                    password = "",
                    phone = it[DoctorEntity.phone]!!,
                    categoryId = it[DoctorEntity.category_id]!!,
                    specializationId = it[DoctorEntity.specialization_id]!!
                )
            }.firstOrNull()

    override fun editDoctor(editDoctorRequest: EditDoctorRequest): Int =
        database.update(DoctorEntity) {
            set(it.email, editDoctorRequest.email)
            set(it.first_name, editDoctorRequest.firstName)
            set(it.last_name, editDoctorRequest.lastName)
            set(it.phone, editDoctorRequest.phone)
            where {
                it.id eq editDoctorRequest.id
            }
        }

    override fun deleteDoctorFromClinic(doctorId: String, clinicId: String): Boolean =
        database.useTransaction {
            var result = database.delete(DoctorClinicEntity) {
                val clinicIdEq = it.clinic_id eq clinicId
                it.doctor_id eq doctorId and clinicIdEq
            } > 0 && database.delete(DoctorWorkTimeEntity) {
                val clinicIdEq = it.clinic_id eq clinicId
                it.doctor_id eq doctorId and clinicIdEq
            } > 0
            val doctorClinicAppointmentIDs: List<Int> = database
                .from(AppointmentEntity)
                .select(AppointmentEntity.id)
                .where { AppointmentEntity.doctor_id eq doctorId and (AppointmentEntity.clinic_id eq clinicId) }
                .map { it[AppointmentEntity.id]!! }
            result = result && database.delete(AppointmentEntity) { it.doctor_id eq doctorId and (it.clinic_id eq clinicId) } > 0
                    && database.delete(AppointmentServiceEntity) { it.appointment_id inList doctorClinicAppointmentIDs } > 0
            if (database.from(DoctorClinicEntity).select().where { DoctorClinicEntity.doctor_id eq doctorId }.map { true }.firstOrNull() == null) {
                result = result && database.delete(DoctorEntity) { it.id eq doctorId } > 0
            }
            return result
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
        val doctorId: String
        val doctor: Doctor? = database.from(DoctorEntity)
            .select()
            .where { DoctorEntity.email eq doctorRegisterRequest.email }
            .map {
                Doctor(
                    id = it[DoctorEntity.id]!!,
                    email = it[DoctorEntity.email]!!,
                    firstName = it[DoctorEntity.first_name]!!,
                    lastName = it[DoctorEntity.last_name]!!,
                    password = "",
                    phone = it[DoctorEntity.phone]!!,
                    categoryId = it[DoctorEntity.category_id]!!,
                    specializationId = it[DoctorEntity.specialization_id]!!
                )
            }
            .firstOrNull()
        if (doctor == null) {
            doctorId = UUID.randomUUID().toString()
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
        } else {
            doctorId = doctor.id
        }
        database.insert(DoctorClinicEntity) {
            set(it.doctor_id, doctorId)
            set(it.clinic_id, clinicId)
        }
        return registerDoctorWorkDays(doctorId, clinicId, doctorRegisterRequest.workDays)
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