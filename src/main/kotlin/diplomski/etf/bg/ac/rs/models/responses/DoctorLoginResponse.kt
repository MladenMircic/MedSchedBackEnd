package diplomski.etf.bg.ac.rs.models.responses

import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.database_models.User
import kotlinx.serialization.Serializable

@Serializable
data class DoctorLoginResponse(
    override var hasEmailError: Boolean = false,
    override var hasPasswordError: Boolean = false,
    override var hasRoleError: Boolean = false,
    override var token: String? = null,
    var doctor: Doctor? = null
) : LoginResponse {

    override fun getUser(): User? = doctor

    override fun getUserPassword(): String? =
        doctor?.password

    override fun setUserNull() {
        doctor = null
    }
}