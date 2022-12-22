package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.User
import diplomski.etf.bg.ac.rs.models.requests.RegisterRequest
import org.koin.core.component.KoinComponent

// Koin component for enabling DI inside derived classes
interface UserDao: KoinComponent {
    fun getUserByEmail(email: String): User?
    fun insertUser(registerRequest: RegisterRequest): Int
}