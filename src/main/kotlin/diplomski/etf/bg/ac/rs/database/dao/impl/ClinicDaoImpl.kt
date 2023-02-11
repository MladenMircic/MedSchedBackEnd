package diplomski.etf.bg.ac.rs.database.dao.impl

import diplomski.etf.bg.ac.rs.database.dao.ClinicDao
import diplomski.etf.bg.ac.rs.database.entities.CategoryEntity
import diplomski.etf.bg.ac.rs.database.entities.DoctorEntity
import diplomski.etf.bg.ac.rs.models.database_models.Category
import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select

class ClinicDaoImpl(private val database: Database): ClinicDao {
    override fun getAllDoctors(): List<Doctor> =
        database
            .from(DoctorEntity)
            .select()
            .map {
                Doctor(
                    id = it[DoctorEntity.id]!!,
                    email = it[DoctorEntity.email]!!,
                    firstName = it[DoctorEntity.first_name]!!,
                    lastName = it[DoctorEntity.last_name]!!,
                    password = "",
                    phone = it[DoctorEntity.last_name]!!,
                    categoryId = it[DoctorEntity.category_id]!!,
                    specializationId = it[DoctorEntity.specialization_id]!!,
                    clinicId = it[DoctorEntity.clinic_id]!!
                )
            }

    override fun getAllCategories(): List<Category> =
        database
            .from(CategoryEntity)
            .select()
            .map {
                Category(
                    id = it[CategoryEntity.id]!!,
                    name = it[CategoryEntity.name]!!
                )
            }
}