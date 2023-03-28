package diplomski.etf.bg.ac.rs.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationData(
    @SerialName("doctor_name")
    val doctorName: String = ""
)