package diplomski.etf.bg.ac.rs.security.services.impl

import diplomski.etf.bg.ac.rs.models.Notification
import diplomski.etf.bg.ac.rs.security.services.OneSignalService
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class OneSignalServiceImpl(
    private val client: HttpClient,
    private val apiKey: String
) : OneSignalService {

    override suspend fun sendNotification(notification: Notification): Boolean {
        return try {
            client.post {
                url(OneSignalService.NOTIFICATIONS)
                contentType(ContentType.Application.Json)
                header("Authorization", "Basic $apiKey")
                setBody(notification)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}