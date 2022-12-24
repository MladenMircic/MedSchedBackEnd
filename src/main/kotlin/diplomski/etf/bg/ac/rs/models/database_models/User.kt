package diplomski.etf.bg.ac.rs.models.database_models

data class User(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val role: Int,
    val phone: String,
    val ssn: String
)