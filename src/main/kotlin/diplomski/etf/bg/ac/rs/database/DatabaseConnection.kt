package diplomski.etf.bg.ac.rs.database

import org.ktorm.database.Database

object DatabaseConnection {
    val database by lazy {
        Database.connect(
            url = "jdbc:mysql://pfw0ltdr46khxib3.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/wvvnsnesvd9hfhoe",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "teoqgnmyw7zvuabg",
            password = "mludvkrxz27mur4f",
            generateSqlInUpperCase = true
        )
    }
}