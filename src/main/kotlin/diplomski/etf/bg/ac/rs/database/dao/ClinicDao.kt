package diplomski.etf.bg.ac.rs.database.dao

import diplomski.etf.bg.ac.rs.models.database_models.Category
import diplomski.etf.bg.ac.rs.models.database_models.Doctor

interface ClinicDao {
    fun getAllDoctors(): List<Doctor>
    fun getAllCategories(): List<Category>
}