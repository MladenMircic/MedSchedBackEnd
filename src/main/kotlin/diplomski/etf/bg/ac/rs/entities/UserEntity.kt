package diplomski.etf.bg.ac.rs.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserEntity: Table<Nothing>("user") {
    val email = varchar("email").primaryKey()
    val password = varchar("password")
    val role = int("role")
    val phone = varchar("phone")
    val ssn = varchar("ssn")
}