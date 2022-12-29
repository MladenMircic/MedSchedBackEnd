package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.varchar

object DoctorEntity: Table<Nothing>("doctor") {
    val email = varchar("email").primaryKey()
    val first_name = varchar("first_name")
    val last_name = varchar("last_name")
    val password = varchar("password")
}