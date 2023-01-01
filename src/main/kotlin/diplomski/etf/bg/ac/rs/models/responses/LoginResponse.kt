package diplomski.etf.bg.ac.rs.models.responses

import diplomski.etf.bg.ac.rs.models.database_models.User
import kotlinx.serialization.Serializable

@Serializable
sealed interface LoginResponse {
    var hasEmailError: Boolean
    var hasPasswordError: Boolean
    val hasRoleError: Boolean
    var token: String?

    fun getUser(): User?
    fun getUserPassword(): String?
    fun setUserNull()
}

//val hasEmailError: Boolean = false,
//val hasPasswordError: Boolean = false,
//val hasRoleError: Boolean = false,
//val token: String? = null,
//val user: User? = null
