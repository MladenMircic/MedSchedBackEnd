package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.User
import diplomski.etf.bg.ac.rs.models.requests.RegisterRequest

interface UserDao {
    fun getUserByEmail(email: String): User?
    fun insertUser(registerRequest: RegisterRequest): Int
}