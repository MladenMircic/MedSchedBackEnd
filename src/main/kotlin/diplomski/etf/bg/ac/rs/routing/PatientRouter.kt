package diplomski.etf.bg.ac.rs.routing

import diplomski.etf.bg.ac.rs.database.dao.PatientDao
import diplomski.etf.bg.ac.rs.models.database_models.Appointment
import diplomski.etf.bg.ac.rs.models.requests.AppointmentsRequest
import diplomski.etf.bg.ac.rs.models.requests.EmailChangeRequest
import diplomski.etf.bg.ac.rs.models.requests.InfoChangeRequest
import diplomski.etf.bg.ac.rs.models.requests.PasswordChangeRequest
import diplomski.etf.bg.ac.rs.models.responses.PasswordChangeResponse
import diplomski.etf.bg.ac.rs.security.services.HashingService
import diplomski.etf.bg.ac.rs.security.services.OneSignalService
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

fun Application.patientRouter() {

    val patientDao: PatientDao by inject()
    val hashingService: HashingService by inject()
    val oneSignalService: OneSignalService by inject()

    routing {
        route("/${Constants.PATIENT_ENDPOINTS}") {
            authenticate(Role.PATIENT.name) {
                get("/allAppointmentsForPatient") {
                    val principal = call.principal<JWTPrincipal>()
                    call.respond(patientDao.getAppointmentsForPatient(
                        principal!!.payload.getClaim("id").asString().toInt()
                    ))
                }

                get("/allCategories") {
                    call.respond(patientDao.getAllCategories())
                }

                get("/getDoctors") {
                    call.respond(patientDao.getDoctors(call.request.queryParameters["category"]?.toInt()))
                }

                get("/getClinics") {
                    call.respond(patientDao.getClinics(call.request.queryParameters["category"]?.toInt()))
                }

                get("/getServicesForDoctor/{doctorId}") {
                    call.respond(patientDao.getAllServicesForDoctor(call.parameters["doctorId"]?.toInt()!!))
                }

                post("/scheduledAppointmentsForDoctor") {
                    val appointmentsRequest = call.receive<AppointmentsRequest>()
                    call.respond(patientDao.getAllAppointmentsForDoctorAtDate(appointmentsRequest))
                }

                post("/scheduleAppointment") {
                    val appointment = call.receive<Appointment>()
                    val appointmentId = patientDao.scheduleAppointment(appointment)
                    try {
                        val appointmentWithDoctor = patientDao.getAppointmentWithDoctorById(appointmentId)
                            ?: throw Exception()
                        call.respond(appointmentWithDoctor)
                    } catch(e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }

                delete("/cancelAppointment/{appointmentId}") {
                    val appointmentId = call.parameters["appointmentId"]?.toInt()!!
                    val callerRole = call.principal<JWTPrincipal>()!!.payload.getClaim("role").asString().toInt()
                    call.respond(
                        if (patientDao.cancelAppointment(appointmentId, callerRole) == 0)
                            HttpStatusCode.InternalServerError
                        else HttpStatusCode.OK
                    )
                }

                post("/updateEmail") {
                    val patientId = call.principal<JWTPrincipal>()!!.payload.getClaim("id").asString().toInt()
                    val emailChangeRequest = call.receive<EmailChangeRequest>()
                    call.respond(
                        if (patientDao.updateEmail(
                                patientId = patientId,
                                email = emailChangeRequest.email
                            ) == 0
                        ) HttpStatusCode.InternalServerError
                        else HttpStatusCode.OK
                    )
                }

                post("/updatePassword") {
                    val patientId = call.principal<JWTPrincipal>()!!.payload.getClaim("id").asString().toInt()
                    val passwordChangeRequest = call.receive<PasswordChangeRequest>()
                    val patient = patientDao.getPatientById(patientId)
                    if (patient == null) {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            PasswordChangeResponse()
                        )
                    }
                    else {
                        if (!hashingService.verifyHash(passwordChangeRequest.oldPassword, patient.password)) {
                            call.respond(
                                HttpStatusCode.InternalServerError,
                                PasswordChangeResponse(oldPasswordCorrect = false)
                            )
                        }
                        else {
                            call.respond(
                                PasswordChangeResponse(
                                    passwordUpdateSuccess = patientDao.updatePassword(
                                        patientId = patientId,
                                        newPassword = hashingService.generateHash(passwordChangeRequest.newPassword)
                                    ) != 0
                                )
                            )
                        }
                    }
                }

                post("/updateInfo") {
                    val patientId = call.principal<JWTPrincipal>()!!.payload.getClaim("id").asString().toInt()
                    val infoChangeRequest = call.receive<InfoChangeRequest>()
                    call.respond(
                        if (patientDao.updateInfo(
                                patientId = patientId,
                                infoChangeRequest = infoChangeRequest
                            ) == 0
                        ) HttpStatusCode.InternalServerError
                        else HttpStatusCode.OK
                    )
                }
            }
        }
    }
}