package diplomski.etf.bg.ac.rs.models.database_models

import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CLINIC")
class Clinic(
    override val id: String,
    override val email: String,
    override var password: String,
    val name: String,
    val openingTime: LocalTime,
    val workHours: Int
) : User