package diplomski.etf.bg.ac.rs.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    @SerialName("include_external_user_ids")
    val includeExternalUserIds: List<String>,
    @SerialName("channel_for_external_user_ids")
    val channelForExternalUserIds: String = "push",
//    @SerialName("included_segments")
//    val includedSegments: List<String>,
    val contents: NotificationMessage,
    val headings: NotificationMessage,
    @SerialName("app_id")
    val appId: String,
)