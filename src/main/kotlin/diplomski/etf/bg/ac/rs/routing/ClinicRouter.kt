package diplomski.etf.bg.ac.rs.routing

import diplomski.etf.bg.ac.rs.database.dao.ClinicDao
import diplomski.etf.bg.ac.rs.utils.Constants
import diplomski.etf.bg.ac.rs.utils.Role
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.clinicRouter() {

    val clinicDao: ClinicDao by inject()

    routing {
        route("/${Constants.CLINIC_ENDPOINTS}") {
            authenticate(Role.CLINIC.name) {
                get("/allDoctors") {
                    call.respond(clinicDao.getAllDoctors())
                }

                get("/allCategories") {
                    call.respond(clinicDao.getAllCategories())
                }
            }
        }
    }
}