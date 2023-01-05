package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DOCTOR")
data class Doctor(
    override val id: Int,
    override val email: String,
    override val firstName: String,
    override val lastName: String,
    override var password: String,
    val phone: String
) : User