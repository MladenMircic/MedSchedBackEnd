package diplomski.etf.bg.ac.rs.routing

import diplomski.etf.bg.ac.rs.database.dao.UserDao
import diplomski.etf.bg.ac.rs.models.requests.LoginRequest
import diplomski.etf.bg.ac.rs.models.requests.RegisterRequest
import diplomski.etf.bg.ac.rs.models.responses.LoginResponse
import diplomski.etf.bg.ac.rs.models.responses.RegisterResponse
import diplomski.etf.bg.ac.rs.security.services.HashingService
import diplomski.etf.bg.ac.rs.security.services.TokenService
import diplomski.etf.bg.ac.rs.security.token.TokenClaim
import diplomski.etf.bg.ac.rs.security.token.TokenConfig
import diplomski.etf.bg.ac.rs.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.userRouter(config: TokenConfig) {

    val userDao: UserDao by inject()
    val hashingService: HashingService by inject()
    val tokenService: TokenService by inject()

    routing {
        route("/${Constants.AUTHENTICATION_ENDPOINTS}") {
            post("/login") {
                val loginRequest = call.receive<LoginRequest>()
                val user = userDao.getUser(loginRequest.email, loginRequest.role)

                if (user == null) {
                    call.respond(
                        LoginResponse(
                            hasEmailError = true
                        )
                    )
                    return@post
                }

                if (!hashingService.verifyHash(loginRequest.password, user.password)) {
                    call.respond(
                        LoginResponse(
                            hasPasswordError = true
                        )
                    )
                    return@post
                } else {
                    val jwtToken = tokenService.generate(
                        config = config,
                        TokenClaim(
                            name = "email",
                            value = user.email
                        ),
                        TokenClaim(
                            name = "role",
                            value = loginRequest.role.toString()
                        )
                    )
                    user.password = ""
                    call.respond(
                        LoginResponse(
                            token = jwtToken,
                            user = user
                        )
                    )
                }
            }

            post("/register") {
                val registerRequest = call.receive<RegisterRequest>()

                if (userDao.emailExists(registerRequest.email)) {
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

            authenticate {
                get("/authenticate") {
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}