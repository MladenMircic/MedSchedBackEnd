package diplomski.etf.bg.ac.rs

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import diplomski.etf.bg.ac.rs.plugins.*

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureSerialization()
    configureRouting()
    configureDependencyInjection()
}
