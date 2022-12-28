package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.Service
import diplomski.etf.bg.ac.rs.models.database_models.User

// General dao for accessing patient related data
interface PatientDao {

    fun getScheduledForPatient(user: User)
    fun getAllServices(): List<Service>
    fun insertService(service: Service): Int
}