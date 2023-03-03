package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DOCTOR")
data class Doctor(
    override val id: String,
    override val email: String,
    val firstName: String,
    val lastName: String,
    override var password: String,
    val phone: String,
    val categoryId: Int,
    val specializationId: Int
) : User