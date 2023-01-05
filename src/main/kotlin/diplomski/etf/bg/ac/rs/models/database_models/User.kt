package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.serialization.Serializable

@Serializable
sealed interface User {
    val id: Int
    val email: String
    val firstName: String
    val lastName: String
    var password: String
}

enum class UserType {
    PATIENT, DOCTOR, CLINIC
}