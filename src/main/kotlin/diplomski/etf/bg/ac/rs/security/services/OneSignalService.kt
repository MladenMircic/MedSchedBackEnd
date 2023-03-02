package diplomski.etf.bg.ac.rs.security.services

import diplomski.etf.bg.ac.rs.models.Notification

interface OneSignalService {

    suspend fun sendNotification(notification: Notification): Boolean

    companion object {
        const val ONESIGNAL_APP_ID = "cb36d618-3937-4e76-a615-22ec5bec1643"

        const val NOTIFICATIONS = "https://onesignal.com/api/v1/notifications"
    }
}