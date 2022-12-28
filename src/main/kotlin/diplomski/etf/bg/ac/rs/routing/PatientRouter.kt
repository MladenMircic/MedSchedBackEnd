package diplomski.etf.bg.ac.rs.routing

import diplomski.etf.bg.ac.rs.database.dao.PatientDao
import diplomski.etf.bg.ac.rs.models.database_models.Service
import diplomski.etf.bg.ac.rs.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.ktor.ext.inject
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun Application.patientRouter() {

    val patientDao: PatientDao by inject()

    routing {
        // Protected static endpoint for getting service icons from server
        authenticate {
            static("/clinic-service") {
                staticRootFolder = File("./images")
                files(".")
            }

            get("/${Constants.PATIENT_ENDPOINTS}/allServices") {
                call.respond(patientDao.getAllServices())
            }
        }
    }
}