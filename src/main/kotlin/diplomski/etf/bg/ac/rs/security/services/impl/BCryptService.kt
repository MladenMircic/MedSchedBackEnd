package diplomski.etf.bg.ac.rs.security.services.impl

import diplomski.etf.bg.ac.rs.security.services.HashingService
import org.mindrot.jbcrypt.BCrypt

class BCryptService: HashingService {

    override fun generateHash(value: String): String {
        return BCrypt.hashpw(value, BCrypt.gensalt())
    }

    override fun verifyHash(value: String, hashed: String): Boolean {
        return BCrypt.checkpw(value, hashed)
    }


}