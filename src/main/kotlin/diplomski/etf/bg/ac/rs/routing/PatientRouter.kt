package diplomski.etf.bg.ac.rs.routing

import diplomski.etf.bg.ac.rs.database.dao.PatientDao
import diplomski.etf.bg.ac.rs.models.Notification
import diplomski.etf.bg.ac.rs.models.notificaton_data.NotificationData
import diplomski.etf.bg.ac.rs.models.NotificationMessage
import diplomski.etf.bg.ac.rs.models.database_models.Appointment
import diplomski.etf.bg.ac.rs.models.database_models.Patient
import diplomski.etf.bg.ac.rs.models.requests.AvailableTimesRequest
import diplomski.etf.bg.ac.rs.models.requests.EmailChangeRequest
import diplomski.etf.bg.ac.rs.models.requests.InfoChangeRequest
import diplomski.etf.bg.ac.rs.models.requests.PasswordChangeRequest
import diplomski.etf.bg.ac.rs.models.responses.PasswordChangeResponse
import diplomski.etf.bg.ac.rs.security.services.HashingService
import diplomski.etf.bg.ac.rs.security.services.OneSignalService
import diplomski.etf.bg.ac.rs.utils.Constants
import diplomski.etf.bg.ac.rs.utils.DateFormatter
import diplomski.etf.bg.ac.rs.utils.Role
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

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
                        principal!!.payload.getClaim("id").asString()
                    ))
                }

                get("/allCategories") {
                    call.respond(patientDao.getAllCategories())
                }

                get("/getDoctors") {
                    val doctorName = call.request.queryParameters["doctor_name"]!!
                    var categoryIds: List<Int> = listOf()
                    categoryIds = if (call.request.queryParameters["categories"]!! != "") {
                        call.request.queryParameters["categories"]!!
                            .split(",")
                            .map { it.toInt() }
                    } else {
                        categoryIds.toMutableList().also { it.add(0) }
                    }
                    call.respond(patientDao.getDoctors(doctorName, categoryIds))
                }

                get("/getClinics") {
                    call.respond(patientDao.getClinics(call.request.queryParameters["category"]?.toInt()))
                }

                get("/getServicesForDoctor/{doctorId}") {
                    call.respond(patientDao.getAllServicesForDoctor(call.parameters["doctorId"]!!))
                }

                post("/availableAppointmentTimes") {
                    val availableTimesRequest = call.receive<AvailableTimesRequest>()
                    call.respond(patientDao.getAvailableTimesForDoctors(availableTimesRequest))
                }

                post("/scheduleAppointments") {
                    val appointmentList = call.receive<List<Appointment>>()
                    try {
                        val idList = patientDao.scheduleAppointments(appointmentList)
                        val patient: Patient = patientDao.getPatientById(appointmentList[0].patientId)!!
                        appointmentList.forEach {
                            oneSignalService.sendNotification(
                                Notification(
                                    includeExternalUserIds =  listOf(it.doctorId),
                                    headings = NotificationMessage(
                                        en = Constants.APPOINTMENT_SCHEDULED_HEADING_EN,
                                        sr = Constants.APPOINTMENT_SCHEDULED_HEADING_SR
                                    ),
                                    contents = NotificationMessage(
                                        en = Constants.APPOINTMENT_SCHEDULED_CONTENT_EN.format(
                                            DateFormatter.dateAsString(it.date),
                                            it.time
                                        ),
                                        sr = Constants.APPOINTMENT_SCHEDULED_CONTENT_SR.format(
                                            DateFormatter.dateAsString(it.date),
                                            it.time
                                        )
                                    ),
                                    data = NotificationData.PatientAppointmentScheduleData(
                                        "${patient.firstName} ${patient.lastName}",
                                        dateOfAction = it.date,
                                        timeOfAction = it.time
                                    ),
                                    appId = OneSignalService.ONESIGNAL_APP_ID
                                )
                            )
                        }
                        call.respond(HttpStatusCode.OK, idList)
                    } catch(e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }

                delete("/cancelAppointment/{appointmentId}") {
                    val appointmentId = call.parameters["appointmentId"]?.toInt()!!
                    val callerRole = call.principal<JWTPrincipal>()!!.payload.getClaim("role").asString().toInt()
                    val cancelledAppointment = patientDao.cancelAppointment(appointmentId, callerRole)
                    cancelledAppointment?.let {
                        val patient: Patient = patientDao.getPatientById(cancelledAppointment.patientId)!!
                        oneSignalService.sendNotification(
                            Notification(
                                includeExternalUserIds = listOf(it.doctorId),
                                headings = NotificationMessage(
                                    en = Constants.APPOINTMENT_CANCELLED_HEADING_EN,
                                    sr = Constants.APPOINTMENT_CANCELLED_HEADING_SR
                                ),
                                contents = NotificationMessage(
                                    en = Constants.APPOINTMENT_CANCELLED_CONTENT_EN.format(
                                        DateFormatter.dateAsString(it.date),
                                        it.time
                                    ),
                                    sr = Constants.APPOINTMENT_CANCELLED_CONTENT_SR.format(
                                        DateFormatter.dateAsString(it.date),
                                        it.time
                                    )
                                ),
                                data = NotificationData.PatientAppointmentCancelData(
                                    patientName = "${patient.firstName} ${patient.lastName}",
                                    dateOfAction = it.date,
                                    timeOfAction = it.time
                                ),
                                appId = OneSignalService.ONESIGNAL_APP_ID
                            )
                        )
                    }
                    call.respond(
                        if (cancelledAppointment == null)
                            HttpStatusCode.NotFound
                        else HttpStatusCode.OK
                    )
                }

                post("/updateEmail") {
                    val patientId = call.principal<JWTPrincipal>()!!.payload.getClaim("id").asString()
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
                    val patientId = call.principal<JWTPrincipal>()!!.payload.getClaim("id").asString()
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
                    val patientId = call.principal<JWTPrincipal>()!!.payload.getClaim("id").asString()
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