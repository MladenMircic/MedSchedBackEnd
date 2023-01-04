package diplomski.etf.bg.ac.rs.routing

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import java.io.File

fun Application.imageRouter() {
    routing {
        authenticate {
            static("/clinic-images") {
                staticRootFolder = File("./images")
                files(".")
            }
        }
    }
}