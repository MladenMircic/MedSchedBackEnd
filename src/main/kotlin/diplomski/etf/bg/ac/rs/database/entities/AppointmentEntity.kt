package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.*

object AppointmentEntity: Table<Nothing>("appointment") {
    val id = int("id").primaryKey()
    val date = date("date")
    val time = time("time")
    val doctor_id = int("doctor_id")
    val patient_id = int("patient_id")
    val exam_name = varchar("exam_name")
}