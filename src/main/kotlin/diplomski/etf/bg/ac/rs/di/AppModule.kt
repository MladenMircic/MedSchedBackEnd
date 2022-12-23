package diplomski.etf.bg.ac.rs.di

import diplomski.etf.bg.ac.rs.database.dao.UserDao
import diplomski.etf.bg.ac.rs.database.dao.impl.UserDaoImpl
import diplomski.etf.bg.ac.rs.security.services.HashingService
import diplomski.etf.bg.ac.rs.security.services.TokenService
import diplomski.etf.bg.ac.rs.security.services.impl.BCryptService
import diplomski.etf.bg.ac.rs.security.services.impl.JwtTokenService
import io.ktor.server.application.*
import org.koin.dsl.module
import org.ktorm.database.Database

val appModule = module {
    single {
        Database.connect(
            url = "jdbc:mysql://pfw0ltdr46khxib3.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/wvvnsnesvd9hfhoe",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "teoqgnmyw7zvuabg",
            password = "mludvkrxz27mur4f",
            generateSqlInUpperCase = true
        )
    }
    single<UserDao> { UserDaoImpl() }
    single<HashingService> { BCryptService() }
    single<TokenService> { JwtTokenService() }
}