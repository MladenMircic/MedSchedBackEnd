package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.UserDao
import diplomski.etf.bg.ac.rs.database.entities.UserEntity
import diplomski.etf.bg.ac.rs.models.database_models.User
import diplomski.etf.bg.ac.rs.models.requests.RegisterRequest
import org.koin.core.component.inject
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

class UserDaoImpl(private val database: Database): UserDao {

    override fun getUserByEmail(email: String): User? =
        database
            .from(UserEntity)
            .select()
            .where {
                UserEntity.email eq email
            }
            .map {
                User(
                    email = it[UserEntity.email]!!,
                    firstName = it[UserEntity.first_name]!!,
                    lastName = it[UserEntity.last_name]!!,
                    password = it[UserEntity.password]!!,
                    role = it[UserEntity.role]!!,
                    phone = it[UserEntity.phone]!!,
                    ssn = it[UserEntity.ssn]!!
                )
            }.firstOrNull()

    override fun insertUser(registerRequest: RegisterRequest): Int =
        database.insert(UserEntity) {
            set(it.email, registerRequest.email)
            set(it.first_name, registerRequest.firstName)
            set(it.last_name, registerRequest.lastName)
            set(it.password, registerRequest.password)
            set(it.role, registerRequest.role)
            set(it.phone, registerRequest.phone)
            set(it.ssn, registerRequest.ssn)
        }
}