package diplomski.etf.bg.ac.rs.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import diplomski.etf.bg.ac.rs.security.token.TokenConfig
import diplomski.etf.bg.ac.rs.utils.Role
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(config: TokenConfig) {
    val realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
    authentication {
        jwt {
            this@jwt.realm = realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
        jwtForRole(realm, config, Role.DOCTOR)
        jwtForRole(realm, config, Role.PATIENT)
    }
}

fun AuthenticationConfig.jwtForRole(realm: String, config: TokenConfig, role: Role) {
    jwt(role.name) {
        this@jwt.realm = realm
        verifier(
            JWT
                .require(Algorithm.HMAC256(config.secret))
                .withAudience(config.audience)
                .withIssuer(config.issuer)
                .build()
        )
        validate { credential ->
            val payload = credential.payload
            if (credential.payload.audience.contains(config.audience) &&
                payload.getClaim("role").asString().toInt() == role.ordinal) {
                JWTPrincipal(credential.payload)
            } else null
        }
    }
}