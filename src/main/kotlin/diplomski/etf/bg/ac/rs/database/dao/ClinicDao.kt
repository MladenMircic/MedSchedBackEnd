package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.Category
import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.database_models.Service
import diplomski.etf.bg.ac.rs.models.requests.DoctorRegisterRequest

interface ClinicDao {
    fun getAllDoctors(): List<Doctor>
    fun getAllCategories(): List<Category>
    fun getAllServicesForCategory(categoryId: Int): List<Service>
    fun registerDoctor(doctorRegisterRequest: DoctorRegisterRequest, clinicId: String): Boolean
    fun deleteDoctor(doctorId: String): Int
}