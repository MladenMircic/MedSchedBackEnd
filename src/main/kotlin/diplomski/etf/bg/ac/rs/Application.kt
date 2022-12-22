package diplomski.etf.bg.ac.rs

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import diplomski.etf.bg.ac.rs.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureDependencyInjection()
    configureSecurity()
}
