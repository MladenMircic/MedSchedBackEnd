package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.PatientDao
import diplomski.etf.bg.ac.rs.database.entities.DoctorEntity
import diplomski.etf.bg.ac.rs.database.entities.ServiceEntity
import diplomski.etf.bg.ac.rs.models.database_models.Service
import diplomski.etf.bg.ac.rs.models.database_models.User
import diplomski.etf.bg.ac.rs.models.database_models.DoctorsForPatient
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

    override fun getDoctors(category: String?): List<DoctorsForPatient> {
        var query = database.from(DoctorEntity).select()
        if (category != null && category != "") {
            println(category)
            query = query.where {
                DoctorEntity.service eq category
            }
        }
        return query.map {
            DoctorsForPatient(
                email = it[DoctorEntity.email]!!,
                firstName = it[DoctorEntity.first_name]!!,
                lastName = it[DoctorEntity.last_name]!!,
                phone = it[DoctorEntity.phone]!!,
                service = it[DoctorEntity.service]!!
            )
        }
    }
}