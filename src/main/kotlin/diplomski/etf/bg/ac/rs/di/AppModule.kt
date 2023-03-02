package diplomski.etf.bg.ac.rs.di

import com.typesafe.config.ConfigFactory
import diplomski.etf.bg.ac.rs.database.DatabaseConnection
import diplomski.etf.bg.ac.rs.database.dao.ClinicDao
import diplomski.etf.bg.ac.rs.database.dao.DoctorDao
import diplomski.etf.bg.ac.rs.database.dao.PatientDao
import diplomski.etf.bg.ac.rs.database.dao.UserDao
import diplomski.etf.bg.ac.rs.database.dao.impl.ClinicDaoImpl
import diplomski.etf.bg.ac.rs.database.dao.impl.DoctorDaoImpl
import diplomski.etf.bg.ac.rs.database.dao.impl.PatientDaoImpl
import diplomski.etf.bg.ac.rs.database.dao.impl.UserDaoImpl
import diplomski.etf.bg.ac.rs.security.services.HashingService
import diplomski.etf.bg.ac.rs.security.services.OneSignalService
import diplomski.etf.bg.ac.rs.security.services.TokenService
import diplomski.etf.bg.ac.rs.security.services.impl.BCryptService
import diplomski.etf.bg.ac.rs.security.services.impl.JwtTokenService
import diplomski.etf.bg.ac.rs.security.services.impl.OneSignalServiceImpl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.dsl.module

val appModule = module {
    single { DatabaseConnection.database }
    single<UserDao> { UserDaoImpl(get()) }
    single<PatientDao> { PatientDaoImpl(get()) }
    single<DoctorDao> { DoctorDaoImpl(get()) }
    single<ClinicDao> { ClinicDaoImpl(get()) }
    single<HashingService> { BCryptService() }
    single<TokenService> { JwtTokenService() }
    // Ktor HTTP client injection rule
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
    }
    single<OneSignalService> {
        val config = ConfigFactory.load()
        OneSignalServiceImpl(
            get(),
            config.getString("onesignal.api_key")
        )
    }
}