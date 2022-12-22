package diplomski.etf.bg.ac.rs.routing

import diplomski.etf.bg.ac.rs.database.dao.UserDao
import diplomski.etf.bg.ac.rs.models.requests.LoginRequest
import diplomski.etf.bg.ac.rs.models.requests.RegisterRequest
import diplomski.etf.bg.ac.rs.models.responses.LoginResponse
import diplomski.etf.bg.ac.rs.models.responses.RegisterResponse
import diplomski.etf.bg.ac.rs.security.services.HashingService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.userRouter() {

    val userDao: UserDao by inject()
    val hashingService: HashingService by inject()

    routing {
        post("/login") {


            val loginRequest = call.receive<LoginRequest>()
            val user = userDao.getUserByEmail(loginRequest.email)

            if (user == null) {
                call.respond(
                    LoginResponse(
                        hasEmailError = true
                    )
                )
            } else {
                call.respond(
                    LoginResponse(
                        hasPasswordError = !hashingService.verifyHash(loginRequest.password, user.password)
                    )
                )
            }
        }

        post("/register") {
            val registerRequest = call.receive<RegisterRequest>()

            val user = userDao.getUserByEmail(registerRequest.email)

            if (user != null) {
                call.respond(
                    RegisterResponse(
                        accountExists = true
                    )
                )
            }
            else {
                registerRequest.password = hashingService.generateHash(registerRequest.password)
                val result = userDao.insertUser(registerRequest)
                if (result > 0) {
                    call.respond(
                        RegisterResponse(
                            success = true
                        )
                    )
                } else {
                    call.respond(
                        RegisterResponse(
                            success = false
                        )
                    )
                }
            }
        }
    }
}