package diplomski.etf.bg.ac.rs.security.services.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import diplomski.etf.bg.ac.rs.security.services.TokenService
import diplomski.etf.bg.ac.rs.security.token.TokenClaim
import diplomski.etf.bg.ac.rs.security.token.TokenConfig
import java.util.*

class JwtTokenService: TokenService {

    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }

}