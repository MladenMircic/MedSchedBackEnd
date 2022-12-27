package diplomski.etf.bg.ac.rs.plugins

import diplomski.etf.bg.ac.rs.routing.patientRouter
import diplomski.etf.bg.ac.rs.routing.userRouter
import diplomski.etf.bg.ac.rs.security.token.TokenConfig
import io.ktor.server.application.*

fun Application.configureRouting(config: TokenConfig) {

    userRouter(config)
    patientRouter()
}
