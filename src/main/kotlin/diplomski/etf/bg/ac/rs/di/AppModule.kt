package diplomski.etf.bg.ac.rs.di

import diplomski.etf.bg.ac.rs.database.dao.UserDao
import diplomski.etf.bg.ac.rs.database.dao.impl.UserDaoImpl
import org.koin.dsl.module
import org.ktorm.database.Database

val appModule = module {
    single {
        Database.connect(
            url = "jdbc:mysql://localhost:3306/med_sched_db",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = "1234"
        )
    }
    single<UserDao> { UserDaoImpl() }
}