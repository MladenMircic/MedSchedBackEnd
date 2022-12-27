package diplomski.etf.bg.ac.rs.di

import diplomski.etf.bg.ac.rs.database.DatabaseConnection
import diplomski.etf.bg.ac.rs.database.dao.PatientDao
import diplomski.etf.bg.ac.rs.database.dao.UserDao
import diplomski.etf.bg.ac.rs.database.dao.impl.PatientDaoImpl
import diplomski.etf.bg.ac.rs.database.dao.impl.UserDaoImpl
import diplomski.etf.bg.ac.rs.security.services.HashingService
import diplomski.etf.bg.ac.rs.security.services.TokenService
import diplomski.etf.bg.ac.rs.security.services.impl.BCryptService
import diplomski.etf.bg.ac.rs.security.services.impl.JwtTokenService
import io.ktor.server.application.*
import org.koin.dsl.module
import org.ktorm.database.Database

val appModule = module {
    single { DatabaseConnection.database }
    single<UserDao> { UserDaoImpl(get()) }
    single<PatientDao> { PatientDaoImpl(get()) }
    single<HashingService> { BCryptService() }
    single<TokenService> { JwtTokenService() }
}