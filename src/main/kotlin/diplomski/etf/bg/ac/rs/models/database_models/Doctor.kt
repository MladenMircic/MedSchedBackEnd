package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.serialization.Serializable

@Serializable
data class Doctor(
    override val email: String,
    override val firstName: String,
    override val lastName: String,
    override var password: String,
    val phone: String
) : User