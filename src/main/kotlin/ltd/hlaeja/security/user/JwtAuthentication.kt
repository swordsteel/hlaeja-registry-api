package ltd.hlaeja.security.user

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

data class JwtAuthentication(
    private val jwtUserDetails: JwtUserDetails,
    private val token: String,
    private var authorities: MutableCollection<out GrantedAuthority>,
    private var authenticated: Boolean = false,
) : Authentication {

    override fun getName(): String = "Bearer Token"

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    override fun getCredentials(): Any = token

    override fun getDetails(): Any? = null

    override fun getPrincipal(): Any = jwtUserDetails

    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }
}
