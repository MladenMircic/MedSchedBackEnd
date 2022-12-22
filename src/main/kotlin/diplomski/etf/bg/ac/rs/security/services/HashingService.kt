package diplomski.etf.bg.ac.rs.security.services

interface HashingService {

    fun generateHash(value: String): String
    fun verifyHash(value: String, hashed: String): Boolean
}