package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.UserDao
import diplomski.etf.bg.ac.rs.database.entities.DoctorEntity
import diplomski.etf.bg.ac.rs.database.entities.PatientEntity
import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.database_models.Patient
import diplomski.etf.bg.ac.rs.models.database_models.User
import diplomski.etf.bg.ac.rs.models.requests.RegisterRequest
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
                            email = it[DoctorEntity.email]!!,
                            firstName = it[DoctorEntity.first_name]!!,
                            lastName = it[DoctorEntity.last_name]!!,
                            password = it[DoctorEntity.password]!!,
                            phone = it[DoctorEntity.phone]!!
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
                null
            }
            else -> {
                null
            }
        }

    override fun insertUser(registerRequest: RegisterRequest): Int =
        when (registerRequest.role) {
            0 -> {
                database.insert(DoctorEntity) {
                    set(it.email, registerRequest.email)
                    set(it.first_name, registerRequest.firstName)
                    set(it.last_name, registerRequest.lastName)
                    set(it.password, registerRequest.password)
                    set(it.phone, registerRequest.phone)
                }
            }
            1 -> {
                database.insert(PatientEntity) {
                    set(it.email, registerRequest.email)
                    set(it.first_name, registerRequest.firstName)
                    set(it.last_name, registerRequest.lastName)
                    set(it.password, registerRequest.password)
                    set(it.phone, registerRequest.phone)
                    set(it.ssn, registerRequest.ssn)
                }
            }
            2 -> {
                0
            }
            else -> {
                0
            }
        }
}