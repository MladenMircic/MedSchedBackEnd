package diplomski.etf.bg.ac.rs.security.services

import diplomski.etf.bg.ac.rs.models.Notification

interface OneSignalService {

    suspend fun sendNotification(notification: Notification): Boolean

    companion object {
        const val ONESIGNAL_APP_ID = "17fc9639-8100-4ae0-9607-19db040c52dd"

        const val NOTIFICATIONS = "https://onesignal.com/api/v1/notifications"
    }
}