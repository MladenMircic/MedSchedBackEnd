package diplomski.etf.bg.ac.rs.security.services.impl

import diplomski.etf.bg.ac.rs.models.Notification
import diplomski.etf.bg.ac.rs.security.services.OneSignalService
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.reflect.*

class OneSignalServiceImpl(
    private val client: HttpClient,
    private val apiKey: String
) : OneSignalService {

    @OptIn(InternalAPI::class)
    override suspend fun sendNotification(notification: Notification): Boolean {
        return try {
            println(apiKey)
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