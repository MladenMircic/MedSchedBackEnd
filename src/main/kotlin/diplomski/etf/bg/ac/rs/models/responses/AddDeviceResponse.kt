package diplomski.etf.bg.ac.rs.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class AddDeviceResponse(
    val success: Boolean,
    val id: String
)