package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object DoctorEntity: Table<Nothing>("doctor") {
    val id = int("id").primaryKey()
    val email = varchar("email")
    val first_name = varchar("first_name")
    val last_name = varchar("last_name")
    val password = varchar("password")
    val phone = varchar("phone")
    val category_id = int("category_id")
    val specialization_id = int("specialization_id")
    val clinic_id = int("clinic_id")
}