package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object PatientEntity: Table<Nothing>("patient") {
    val id = varchar("id").primaryKey()
    val email = varchar("email")
    val first_name = varchar("first_name")
    val last_name = varchar("last_name")
    val password = varchar("password")
    val phone = varchar("phone")
    val ssn = varchar("ssn")
}