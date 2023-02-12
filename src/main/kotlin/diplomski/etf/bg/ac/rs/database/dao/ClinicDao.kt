package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.Category
import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.requests.DoctorRegisterRequest

interface ClinicDao {
    fun getAllDoctors(): List<Doctor>
    fun getAllCategories(): List<Category>
    fun registerDoctor(doctorRegisterRequest: DoctorRegisterRequest, clinicId: Int): Boolean
    fun deleteDoctor(doctorId: Int): Int
}