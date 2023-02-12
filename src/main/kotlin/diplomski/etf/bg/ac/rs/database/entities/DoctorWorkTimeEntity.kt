package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.time

object DoctorWorkTimeEntity: Table<Nothing>("doctor_work_time") {
    val id = int("id").primaryKey()
    val doctor_id = int("doctor_id")
    val clinic_id = int("clinic_id")
    val day_of_week = int("day_of_week")
    val time = time("time")
}