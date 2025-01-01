package ltd.hlaeja.security

import java.util.UUID

data class JwtUserDetails(
    val id: UUID,
    val username: String,
)
