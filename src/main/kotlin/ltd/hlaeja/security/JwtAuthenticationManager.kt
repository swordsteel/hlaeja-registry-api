package ltd.hlaeja.security

import ltd.hlaeja.security.user.JwtAuthentication
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationManager : ReactiveAuthenticationManager {

    override fun authenticate(
        authentication: Authentication,
    ): Mono<Authentication> = if (authentication is JwtAuthentication) {
        handleJwtToken(authentication)
    } else {
        Mono.error(object : AuthenticationException("Unsupported authentication type") {})
    }

    private fun handleJwtToken(
        token: JwtAuthentication,
    ): Mono<Authentication> = if (token.isAuthenticated) {
        Mono.just(token)
    } else {
        Mono.error(object : AuthenticationException("Invalid or expired JWT token") {})
    }
}
