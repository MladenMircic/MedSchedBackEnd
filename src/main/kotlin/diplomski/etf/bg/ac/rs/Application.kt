package diplomski.etf.bg.ac.rs

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import diplomski.etf.bg.ac.rs.plugins.*
import diplomski.etf.bg.ac.rs.security.services.TokenService
import diplomski.etf.bg.ac.rs.security.token.TokenConfig
import org.koin.ktor.ext.inject
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureDependencyInjection()
    configureSerialization()
    configureRouting()

    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = TimeUnit.SECONDS.toMillis(1),
        secret = System.getenv("JWT_SECRET")
    )
    configureSecurity(tokenConfig)
}
