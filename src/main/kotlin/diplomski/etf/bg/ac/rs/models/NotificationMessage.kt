package diplomski.etf.bg.ac.rs.models

import kotlinx.serialization.Serializable

@Serializable
data class NotificationMessage(
    val en: String,
    val sr: String
)