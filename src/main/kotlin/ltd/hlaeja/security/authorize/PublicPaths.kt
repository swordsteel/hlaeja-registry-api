package ltd.hlaeja.security.authorize

import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec

fun AuthorizeExchangeSpec.publicPaths(): AuthorizeExchangeSpec.Access = pathMatchers(
    "/actuator/**",
    "/login",
)
