package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.database.DatabaseConnection
import diplomski.etf.bg.ac.rs.database.entities.UserEntity
import diplomski.etf.bg.ac.rs.models.database_models.User
import diplomski.etf.bg.ac.rs.models.requests.RegisterRequest
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

class UserDao {

    private val database = DatabaseConnection.database

    fun getUserByEmail(email: String): User? =
        database
            .from(UserEntity)
            .select()
            .where {
                UserEntity.email eq email
            }
            .map {
                User(
                    email = it[UserEntity.email]!!,
                    password = it[UserEntity.password]!!,
                    role = it[UserEntity.role]!!,
                    phone = it[UserEntity.phone]!!,
                    ssn = it[UserEntity.ssn]!!
                )
            }.firstOrNull()

    fun insertUser(registerRequest: RegisterRequest) =
        database.insert(UserEntity) {
            set(it.email, registerRequest.email)
            set(it.password, BCrypt.hashpw(registerRequest.password, BCrypt.gensalt()))
            set(it.role, registerRequest.role)
            set(it.phone, registerRequest.phone)
            set(it.ssn, registerRequest.ssn)
        }
}