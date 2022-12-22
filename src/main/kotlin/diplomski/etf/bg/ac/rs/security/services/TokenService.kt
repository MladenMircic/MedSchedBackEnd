package diplomski.etf.bg.ac.rs.security.services

import diplomski.etf.bg.ac.rs.security.token.TokenClaim
import diplomski.etf.bg.ac.rs.security.token.TokenConfig

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String
}