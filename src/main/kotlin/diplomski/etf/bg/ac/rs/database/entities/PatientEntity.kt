package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

// Role values:
// 0 (Doctor)
// 1 (Patient)
// 2 (Administrator)
object PatientEntity: Table<Nothing>("patient") {
    val email = varchar("email").primaryKey()
    val first_name = varchar("first_name")
    val last_name = varchar("last_name")
    val password = varchar("password")
    val role = int("role")
    val phone = varchar("phone")
    val ssn = varchar("ssn")
}