package ltd.hlaeja.security.user

import java.util.UUID

data class JwtUserDetails(
    val id: UUID,
    val username: String,
)
