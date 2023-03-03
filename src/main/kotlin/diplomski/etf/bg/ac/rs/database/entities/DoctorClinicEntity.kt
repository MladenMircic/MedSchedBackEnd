package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object DoctorClinicEntity: Table<Nothing>("doctor_clinic") {
    val id = int("id").primaryKey()
    val clinic_id = varchar("clinic_id")
    val doctor_id = varchar("doctor_id")
}