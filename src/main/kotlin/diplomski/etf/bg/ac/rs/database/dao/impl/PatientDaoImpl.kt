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

    override fun proba(): Service? =
        database
            .from(ServiceEntity)
            .select()
            .where {
                ServiceEntity.id eq 2
            }
            .map {
                Service(
                    id = it[ServiceEntity.id]!!,
                    name = it[ServiceEntity.name]!!,
                    icon = it[ServiceEntity.icon]!!
                )
            }.firstOrNull()

    override fun getAllServices(): List<Service> =
        database
            .from(ServiceEntity)
            .select()
            .map {
                Service(
                    id = it[ServiceEntity.id]!!,
                    name = it[ServiceEntity.name]!!,
                    icon = it[ServiceEntity.icon]!!
                )
            }

    override fun insertService(service: Service): Int =
        database.insert(ServiceEntity) {
            set(it.name, service.name)
            set(it.icon, service.icon)
        }
}