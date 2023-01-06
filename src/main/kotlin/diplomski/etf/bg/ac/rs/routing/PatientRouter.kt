package diplomski.etf.bg.ac.rs.routing

import diplomski.etf.bg.ac.rs.database.dao.PatientDao
import diplomski.etf.bg.ac.rs.models.database_models.Appointment
import diplomski.etf.bg.ac.rs.models.requests.AppointmentsRequest
import diplomski.etf.bg.ac.rs.utils.Constants
import diplomski.etf.bg.ac.rs.utils.Role
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

fun Application.patientRouter() {

    val patientDao: PatientDao by inject()
    routing {
        route("/${Constants.PATIENT_ENDPOINTS}") {
            authenticate(Role.PATIENT.name) {
                get("/allCategories") {
                    call.respond(patientDao.getAllCategories())
                }

                get("/getDoctors") {
                    call.respond(patientDao.getDoctors(call.request.queryParameters["category"]))
                }

                get("/getServicesForDoctor/{doctorId}") {
                    call.respond(patientDao.getAllServicesForDoctor(call.parameters["doctorId"]?.toInt() ?: 1))
                }

                post("/scheduledAppointmentsForDoctor") {
                    val appointmentsRequest = call.receive<AppointmentsRequest>()
                    call.respond(patientDao.getAllAppointmentsForDoctorAtDate(appointmentsRequest))
                }
            }
        }
    }
}