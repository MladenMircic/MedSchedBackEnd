package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.Category
import diplomski.etf.bg.ac.rs.models.database_models.Doctor
import diplomski.etf.bg.ac.rs.models.database_models.Service
import diplomski.etf.bg.ac.rs.models.requests.DoctorRegisterRequest
import diplomski.etf.bg.ac.rs.models.requests.EditDoctorRequest

interface ClinicDao {
    fun getAllDoctorsForClinic(clinicId: String): List<Doctor>
    fun getDoctorByEmail(email: String): Doctor?
    fun editDoctor(editDoctorRequest: EditDoctorRequest): Int
    fun deleteDoctorFromClinic(doctorId: String, clinicId: String): Boolean
    fun getAllCategories(): List<Category>
    fun getAllServicesForCategory(categoryId: Int): List<Service>
    fun registerDoctor(doctorRegisterRequest: DoctorRegisterRequest, clinicId: String): Boolean
}