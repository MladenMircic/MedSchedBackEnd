package diplomski.etf.bg.ac.rs.routing

import diplomski.etf.bg.ac.rs.database.dao.DoctorDao
import diplomski.etf.bg.ac.rs.utils.Constants
import diplomski.etf.bg.ac.rs.utils.Role
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.doctorRouter() {

    val doctorDao: DoctorDao by inject()

    routing {
        route("/${Constants.DOCTOR_ENDPOINTS}") {
            authenticate(Role.DOCTOR.name) {
                get("/allAppointmentsForDoctor") {
                    val principal = call.principal<JWTPrincipal>()
                    call.respond(doctorDao.getAppointmentsForDoctor(
                        principal!!.payload.getClaim("id").asString().toInt()
                    ))
                }
            }
        }
    }
}