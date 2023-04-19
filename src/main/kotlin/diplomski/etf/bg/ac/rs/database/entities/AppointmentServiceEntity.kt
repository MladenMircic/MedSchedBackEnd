package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int

object AppointmentServiceEntity : Table<Nothing>("appointment_service") {
    val id = int("id").primaryKey()
    val appointment_id = int("appointment_id")
    val service_id = int("service_id")
}