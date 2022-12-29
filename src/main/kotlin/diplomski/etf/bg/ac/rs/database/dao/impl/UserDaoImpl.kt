package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.UserDao
import diplomski.etf.bg.ac.rs.database.entities.PatientEntity
import diplomski.etf.bg.ac.rs.models.database_models.User
import diplomski.etf.bg.ac.rs.models.requests.RegisterRequest
import org.ktorm.database.Database
import org.ktorm.dsl.*

class UserDaoImpl(private val database: Database): UserDao {

    override fun getUserByEmail(email: String): User? =
        database
            .from(PatientEntity)
            .select()
            .where {
                PatientEntity.email eq email
            }
            .map {
                User(
                    email = it[PatientEntity.email]!!,
                    firstName = it[PatientEntity.first_name]!!,
                    lastName = it[PatientEntity.last_name]!!,
                    password = it[PatientEntity.password]!!,
                    role = it[PatientEntity.role]!!,
                    phone = it[PatientEntity.phone]!!,
                    ssn = it[PatientEntity.ssn]!!
                )
            }.firstOrNull()

    override fun insertUser(registerRequest: RegisterRequest): Int =
        database.insert(PatientEntity) {
            set(it.email, registerRequest.email)
            set(it.first_name, registerRequest.firstName)
            set(it.last_name, registerRequest.lastName)
            set(it.password, registerRequest.password)
            set(it.role, registerRequest.role)
            set(it.phone, registerRequest.phone)
            set(it.ssn, registerRequest.ssn)
        }
}