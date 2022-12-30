package diplomski.etf.bg.ac.rs.models.database_models

interface User {
    val email: String
    val firstName: String
    val lastName: String
    val password: String
}