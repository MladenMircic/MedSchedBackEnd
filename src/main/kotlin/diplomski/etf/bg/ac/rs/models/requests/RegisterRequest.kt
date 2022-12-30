package diplomski.etf.bg.ac.rs.models.requests

import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.database_models.Patient
import diplomski.etf.bg.ac.rs.models.database_models.User
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    var password: String,
    val role: Int,
    val phone: String,
    val ssn: String
) {

    fun toPatient(): Patient {
        return Patient(
            email = email,
            firstName = firstName,
            lastName = lastName,
            password = password,
            phone = phone,
            ssn = ssn
        )
    }

    fun toDoctor(): Doctor {
        return Doctor(
            email = email,
            firstName = firstName,
            lastName = lastName,
            password = password,
            phone = phone
        )
    }
}
