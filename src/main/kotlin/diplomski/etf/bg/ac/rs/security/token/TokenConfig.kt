package diplomski.etf.bg.ac.rs.security.token

import java.util.concurrent.TimeUnit

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String
)