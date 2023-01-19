package diplomski.etf.bg.ac.rs.database.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object ServiceEntity: Table<Nothing>("service") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val category_id = int("category_id")
}