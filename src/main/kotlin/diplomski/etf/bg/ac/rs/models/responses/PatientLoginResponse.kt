package diplomski.etf.bg.ac.rs.models.responses

import diplomski.etf.bg.ac.rs.models.database_models.Patient
import diplomski.etf.bg.ac.rs.models.database_models.User
import kotlinx.serialization.Serializable

@Serializable
data class PatientLoginResponse(
    override var hasEmailError: Boolean = false,
    override var hasPasswordError: Boolean = false,
    override var hasRoleError: Boolean = false,
    override var token: String? = null,
    var patient: Patient? = null
) : LoginResponse {

    override fun getUser(): User? = patient

    override fun getUserPassword(): String? =
        patient?.password

    override fun setUserNull() {
        patient = null
    }
}
