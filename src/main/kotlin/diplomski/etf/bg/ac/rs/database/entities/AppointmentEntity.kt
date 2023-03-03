package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.*

object AppointmentEntity: Table<Nothing>("appointment") {
    val id = int("id").primaryKey()
    val date = date("date")
    val time = time("time")
    val doctor_id = varchar("doctor_id")
    val patient_id = varchar("patient_id")
    val exam_id = int("exam_id")
    val confirmed = boolean("confirmed")
    val cancelled_by = int("cancelled_by")
}