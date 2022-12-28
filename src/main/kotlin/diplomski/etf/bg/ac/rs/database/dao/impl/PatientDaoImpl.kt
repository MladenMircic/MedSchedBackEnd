package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.PatientDao
import diplomski.etf.bg.ac.rs.database.entities.ServiceEntity
import diplomski.etf.bg.ac.rs.models.database_models.Service
import diplomski.etf.bg.ac.rs.models.database_models.User
import org.ktorm.database.Database
import org.ktorm.dsl.*

class PatientDaoImpl(private val database: Database): PatientDao {

    override fun getScheduledForPatient(user: User) {
        // TODO implement this
    }

    override fun getAllServices(): List<Service> =
        database
            .from(ServiceEntity)
            .select()
            .map {
                Service(
                    name = it[ServiceEntity.name]!!
                )
            }

    override fun insertService(service: Service): Int =
        database.insert(ServiceEntity) {
            set(it.name, service.name)
        }
}