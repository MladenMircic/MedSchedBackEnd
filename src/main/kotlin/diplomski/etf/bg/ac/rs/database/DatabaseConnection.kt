package diplomski.etf.bg.ac.rs.database

import org.ktorm.database.Database

object DatabaseConnection {
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/med_sched_db",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "1234"
    )
}