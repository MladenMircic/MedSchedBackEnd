package diplomski.etf.bg.ac.rs.di

import diplomski.etf.bg.ac.rs.database.dao.UserDao
import diplomski.etf.bg.ac.rs.database.dao.impl.UserDaoImpl
import org.koin.dsl.module

val appModule = module {
    single<UserDao> { UserDaoImpl() }
}