package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PATIENT")
data class Patient(
    override val id: String,
    override val email: String,
    val firstName: String,
    val lastName: String,
    override var password: String,
    val phone: String,
    val ssn: String
) : User