package diplomski.etf.bg.ac.rs.routing

import diplomski.etf.bg.ac.rs.database.dao.DoctorDao
import diplomski.etf.bg.ac.rs.security.services.OneSignalService
import diplomski.etf.bg.ac.rs.utils.Constants
import diplomski.etf.bg.ac.rs.utils.Role
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.doctorRouter() {

    val doctorDao: DoctorDao by inject()
    val oneSignalService: OneSignalService by inject()

    routing {
        route("/${Constants.DOCTOR_ENDPOINTS}") {
            authenticate(Role.DOCTOR.name) {
                get("/allAppointmentsForDoctor") {
                    val principal = call.principal<JWTPrincipal>()
                    call.respond(doctorDao.getAppointmentsForDoctor(
                        principal!!.payload.getClaim("id").asString()
                    ))
                }

                delete("/cancelAppointment/{appointmentId}") {
                    val appointmentId = call.parameters["appointmentId"]?.toInt()!!
                    val callerRole = call.principal<JWTPrincipal>()!!.payload.getClaim("role").asString().toInt()
                    call.respond(
                        if (doctorDao.cancelAppointment(appointmentId, callerRole) == 0)
                            HttpStatusCode.NotFound
                        else HttpStatusCode.OK
                    )
                }

                delete("/dismissAppointment/{appointmentId}") {
                    val appointmentId = call.parameters["appointmentId"]?.toInt()!!
                    call.respond(
                        if (!doctorDao.dismissAppointment(appointmentId))
                            HttpStatusCode.NotFound
                        else HttpStatusCode.OK
                    )
                }
            }
        }
    }
}