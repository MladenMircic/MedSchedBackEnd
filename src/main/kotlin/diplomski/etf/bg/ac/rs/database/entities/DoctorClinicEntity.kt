package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int

object DoctorClinicEntity: Table<Nothing>("doctor_clinic") {
    val id = int("id").primaryKey()
    val clinic_id = int("clinic_id")
    val doctor_id = int("doctor_id")
}