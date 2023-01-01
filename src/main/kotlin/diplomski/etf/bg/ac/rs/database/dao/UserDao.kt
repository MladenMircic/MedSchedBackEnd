package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.database_models.Patient
import diplomski.etf.bg.ac.rs.models.database_models.User
import diplomski.etf.bg.ac.rs.models.requests.RegisterRequest
import org.koin.core.component.KoinComponent

// Koin component for enabling DI inside derived classes
interface UserDao: KoinComponent {
    fun emailExists(email: String): Boolean

    fun getUser(email: String, role: Int): User?
    fun insertUser(registerRequest: RegisterRequest): Int
}