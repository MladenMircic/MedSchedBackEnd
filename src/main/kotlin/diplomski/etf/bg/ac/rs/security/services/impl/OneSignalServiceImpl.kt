package diplomski.etf.bg.ac.rs.security.services.impl

import diplomski.etf.bg.ac.rs.models.Notification
import diplomski.etf.bg.ac.rs.security.services.OneSignalService
import io.ktor.client.*

class OneSignalServiceImpl(
    private val client: HttpClient,
    private val apiKey: String
) : OneSignalService {

    override suspend fun sendNotification(notification: Notification): Boolean {
        println(apiKey)
        return true
    }
}