package ltd.hlaeja.configuration

import ltd.hlaeja.security.JwtAuthenticationConverter
import ltd.hlaeja.security.manager.JwtAuthenticationManager
import ltd.hlaeja.security.authorize.publicPaths
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityWebFilterChain(
        serverHttpSecurity: ServerHttpSecurity,
        jwtAuthenticationManager: JwtAuthenticationManager,
        jwtAuthenticationConverter: JwtAuthenticationConverter,
    ): SecurityWebFilterChain = serverHttpSecurity
        .authorizeExchange(::authorizeExchange)
        .httpBasic(::httpBasic)
        .formLogin(::formLogin)
        .csrf(::csrf)
        .addFilterAt(
            AuthenticationWebFilter(jwtAuthenticationManager).apply {
                setServerAuthenticationConverter(jwtAuthenticationConverter)
            },
            AUTHENTICATION,
        )
        .build()

    private fun csrf(
        csrf: CsrfSpec,
    ) = csrf.disable()

    private fun formLogin(
        formLogin: FormLoginSpec,
    ) = formLogin.disable()

    private fun httpBasic(
        httpBasic: HttpBasicSpec,
    ) = httpBasic.disable()

    private fun authorizeExchange(
        authorizeExchange: AuthorizeExchangeSpec,
    ) = authorizeExchange
        .publicPaths().permitAll()
        .anyExchange().hasRole("REGISTRY")
}
