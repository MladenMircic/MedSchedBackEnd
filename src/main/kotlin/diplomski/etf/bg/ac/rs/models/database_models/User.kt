package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.serialization.Serializable

@Serializable
sealed interface User {
    val id: String
    val email: String
    var password: String
}

enum class UserType {
    PATIENT, DOCTOR, CLINIC
}