package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.blob
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object ServiceEntity: Table<Nothing>("service") {
    val name = varchar("name").primaryKey()
}