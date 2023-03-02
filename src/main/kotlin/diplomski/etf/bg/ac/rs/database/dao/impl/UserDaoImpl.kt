package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.UserDao
import diplomski.etf.bg.ac.rs.database.entities.ClinicEntity
import diplomski.etf.bg.ac.rs.database.entities.DoctorEntity
import diplomski.etf.bg.ac.rs.database.entities.PatientEntity
import diplomski.etf.bg.ac.rs.models.database_models.Clinic
import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.database_models.Patient
import diplomski.etf.bg.ac.rs.models.database_models.User
import diplomski.etf.bg.ac.rs.models.requests.RegisterRequest
import kotlinx.datetime.toKotlinLocalTime
import org.ktorm.database.Database
import org.ktorm.dsl.*

class UserDaoImpl(private val database: Database): UserDao {

    override fun emailExists(email: String): Boolean {
        val patientExists = database
            .from(PatientEntity)
            .select()
            .where {
                PatientEntity.email eq email
            }.totalRecords > 0
        val doctorExists = database
            .from(DoctorEntity)
            .select()
            .where {
                DoctorEntity.email eq email
            }.totalRecords > 0
        return patientExists || doctorExists
    }

    override fun getUser(email: String, role: Int): User? =
        when (role) {
            0 -> {
                database.from(DoctorEntity)
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
                            password = it[DoctorEntity.password]!!,
                            phone = it[DoctorEntity.phone]!!,
                            categoryId = it[DoctorEntity.category_id]!!,
                            specializationId = it[DoctorEntity.specialization_id]!!,
                            clinicId = it[DoctorEntity.clinic_id]!!
                        )
                    }.firstOrNull()
            }
            1 -> {
                database.from(PatientEntity)
                    .select()
                    .where {
                        PatientEntity.email eq email
                    }
                    .map {
                        Patient(
                            id = it[PatientEntity.id]!!,
                            email = it[PatientEntity.email]!!,
                            firstName = it[PatientEntity.first_name]!!,
                            lastName = it[PatientEntity.last_name]!!,
                            password = it[PatientEntity.password]!!,
                            phone = it[PatientEntity.phone]!!,
                            ssn = it[PatientEntity.ssn]!!
                        )
                    }.firstOrNull()
            }
            2 -> {
                database.from(ClinicEntity)
                    .select()
                    .where {
                        ClinicEntity.email eq email
                    }
                    .map {
                        Clinic(
                            id = it[ClinicEntity.id]!!,
                            email = it[ClinicEntity.email]!!,
                            password = it[ClinicEntity.password]!!,
                            name = it[ClinicEntity.name]!!,
                            openingTime = it[ClinicEntity.opening_time]!!.toKotlinLocalTime(),
                            workHours = it[ClinicEntity.work_hours]!!
                        )
                    }.firstOrNull()
            }
            else -> {
                null
            }
        }

    override fun insertUser(registerRequest: RegisterRequest): Int =
        when (registerRequest.role) {
            0 -> {
                database.insertAndGenerateKey(DoctorEntity) {
                    set(it.email, registerRequest.email)
                    set(it.first_name, registerRequest.firstName)
                    set(it.last_name, registerRequest.lastName)
                    set(it.password, registerRequest.password)
                    set(it.phone, registerRequest.phone)
                } as Int
            }
            1 -> {
                database.insertAndGenerateKey(PatientEntity) {
                    set(it.email, registerRequest.email)
                    set(it.first_name, registerRequest.firstName)
                    set(it.last_name, registerRequest.lastName)
                    set(it.password, registerRequest.password)
                    set(it.phone, registerRequest.phone)
                    set(it.ssn, registerRequest.ssn)
                } as Int
            }
            2 -> {
                0
            }
            else -> {
                0
            }
        }
}