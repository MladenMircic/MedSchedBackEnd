package diplomski.etf.bg.ac.rs.plugins

import diplomski.etf.bg.ac.rs.routing.userRouter
import io.ktor.server.application.*

fun Application.configureRouting() {

    userRouter()
}
