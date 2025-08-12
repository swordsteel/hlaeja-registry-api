package ltd.hlaeja.security

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.JwtException
import java.util.UUID
import ltd.hlaeja.jwt.service.PublicJwtService
import ltd.hlaeja.security.user.JwtAuthentication
import ltd.hlaeja.security.user.JwtUserDetails
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger {}

@Component
class JwtAuthenticationConverter(
    private val publicJwtService: PublicJwtService,
) : ServerAuthenticationConverter {

    companion object {
        private const val BEARER = "Bearer "
        private const val AUTHORIZATION = "Authorization"
    }

    override fun convert(
        exchange: ServerWebExchange,
    ): Mono<Authentication> = Mono.justOrEmpty(exchange.request.headers.getFirst(AUTHORIZATION))
        .filter { it.startsWith(BEARER) }
        .map { it.removePrefix(BEARER) }
        .flatMap { token ->
            try {
                Mono.just(jwtAuthenticationToken(token))
            } catch (e: JwtException) {
                log.error(e) { "${e.message}" }
                Mono.error(ResponseStatusException(UNAUTHORIZED))
            }
        }

    private fun jwtAuthenticationToken(token: String) = publicJwtService.verify(token) { claims ->
        JwtAuthentication(
            JwtUserDetails(
                UUID.fromString(claims.payload["id"] as String),
                claims.payload["username"] as String,
            ),
            token,
            (claims.payload["role"] as String).split(",")
                .map { SimpleGrantedAuthority(it) }
                .toMutableList(),
            true,
        )
    }
}
