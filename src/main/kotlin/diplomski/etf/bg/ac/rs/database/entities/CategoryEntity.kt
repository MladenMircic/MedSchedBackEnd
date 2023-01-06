package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.varchar

object CategoryEntity: Table<Nothing>("category") {
    val name = varchar("name").primaryKey()
}