package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.time
import org.ktorm.schema.varchar

object ClinicEntity: Table<Nothing>("clinic") {
    val id = varchar("id").primaryKey()
    val email = varchar("email")
    val password = varchar("password")
    val name = varchar("name")
    val opening_time = time("opening_time")
    val work_hours = int("work_hours")
}