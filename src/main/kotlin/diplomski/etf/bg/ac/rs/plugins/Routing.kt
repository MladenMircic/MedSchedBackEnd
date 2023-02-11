package diplomski.etf.bg.ac.rs.plugins

import diplomski.etf.bg.ac.rs.routing.*
import diplomski.etf.bg.ac.rs.security.token.TokenConfig
import io.ktor.server.application.*

fun Application.configureRouting(config: TokenConfig) {

    userRouter(config)
    patientRouter()
    doctorRouter()
    clinicRouter()
    imageRouter()
}
