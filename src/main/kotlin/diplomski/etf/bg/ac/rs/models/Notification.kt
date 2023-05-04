package diplomski.etf.bg.ac.rs.models

import diplomski.etf.bg.ac.rs.models.notificaton_data.NotificationData
import diplomski.etf.bg.ac.rs.utils.Constants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    @SerialName("include_external_user_ids")
    val includeExternalUserIds: List<String>,
    @SerialName("channel_for_external_user_ids")
    val channelForExternalUserIds: String = Constants.CHANNEL_FOR_EXTERNAL_IDS,
    val headings: NotificationMessage,
    val contents: NotificationMessage,
    @SerialName("small_icon")
    val smallIcon: String = Constants.NOTIFICATION_ICON,
    @SerialName("app_id")
    val appId: String,
    @SerialName("data")
    val data: NotificationData? = null
)