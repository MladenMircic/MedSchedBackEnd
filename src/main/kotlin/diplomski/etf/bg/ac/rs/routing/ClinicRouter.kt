package diplomski.etf.bg.ac.rs.routing

import diplomski.etf.bg.ac.rs.database.dao.ClinicDao
import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.requests.DoctorRegisterRequest
import diplomski.etf.bg.ac.rs.models.requests.EditDoctorRequest
import diplomski.etf.bg.ac.rs.security.services.HashingService
import diplomski.etf.bg.ac.rs.utils.Constants
import diplomski.etf.bg.ac.rs.utils.Role
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.clinicRouter() {

    val clinicDao: ClinicDao by inject()
    val hashingService: HashingService by inject()

    routing {
        route("/${Constants.CLINIC_ENDPOINTS}") {
            authenticate(Role.CLINIC.name) {
                get("/allDoctors") {
                    call.respond(clinicDao.getAllDoctors())
                }

                post("/editDoctor") {
                    val editDoctorRequest = call.receive<EditDoctorRequest>()
                    call.respond(
                        if (clinicDao.editDoctor(editDoctorRequest) != 0)
                            HttpStatusCode.OK
                        else
                            HttpStatusCode.NotFound
                    )
                }

                get("/allCategories") {
                    call.respond(clinicDao.getAllCategories())
                }

                get("/allServicesForCategory") {
                    val categoryId = call.request.queryParameters["categoryId"]?.toInt() ?: -1
                    call.respond(clinicDao.getAllServicesForCategory(categoryId))
                }

                post("/registerDoctor") {
                    val doctorRegisterRequest = call.receive<DoctorRegisterRequest>()
                    val principal = call.principal<JWTPrincipal>()
                    doctorRegisterRequest.password = hashingService.generateHash(doctorRegisterRequest.password)
                    val operationSuccess = clinicDao.registerDoctor(
                        doctorRegisterRequest,
                        principal!!.payload.getClaim("id").asString()
                    )
                    call.respond(
                        if (!operationSuccess)
                            HttpStatusCode.InternalServerError
                        else HttpStatusCode.OK
                    )
                }

                delete("/deleteDoctor") {
                    val doctorId = call.request.queryParameters["doctorId"] ?: ""
                    call.respond(
                        if (clinicDao.deleteDoctor(doctorId) == 0)
                            HttpStatusCode.InternalServerError
                        else HttpStatusCode.OK
                    )
                }
            }
        }
    }
}