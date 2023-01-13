package diplomski.etf.bg.ac.rs

import diplomski.etf.bg.ac.rs.plugins.configureDependencyInjection
import diplomski.etf.bg.ac.rs.plugins.configureRouting
import diplomski.etf.bg.ac.rs.plugins.configureSecurity
import diplomski.etf.bg.ac.rs.plugins.configureSerialization
import diplomski.etf.bg.ac.rs.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.netty.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    if (Files.notExists(Paths.get("./images"))) {
        Files.createDirectory(Paths.get("./images"))
    }
    if (Files.notExists(Paths.get("./images/services"))) {
        Files.createDirectory(Paths.get("./images/services"))
    }
    if (Files.notExists(Paths.get("./images/doctors"))) {
        Files.createDirectory(Paths.get("./images/doctors"))
    }
    configureDependencyInjection()
    configureSerialization()

    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = TimeUnit.DAYS.toMillis(365L),
        secret = System.getenv("JWT_SECRET")
    )
    configureSecurity(tokenConfig)
    configureRouting(tokenConfig)
}
