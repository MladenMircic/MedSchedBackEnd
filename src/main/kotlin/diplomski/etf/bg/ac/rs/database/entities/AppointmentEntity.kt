package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.*

object AppointmentEntity: Table<Nothing>("appointment") {
    val id = int("id").primaryKey()
    val date = date("date")
    val time = time("time")
    val doctor_id = varchar("doctor_id")
    val clinic_id = varchar("clinic_id")
    val patient_id = varchar("patient_id")
    val confirmed = boolean("confirmed")
    val cancelled_by = int("cancelled_by")
}