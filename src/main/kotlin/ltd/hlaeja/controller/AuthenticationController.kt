package ltd.hlaeja.controller

import ltd.hlaeja.library.accountRegistry.Authentication
import ltd.hlaeja.service.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(
    private val authenticationService: AuthenticationService,
) {

    @PostMapping("/login")
    suspend fun addDevice(
        @RequestBody request: Authentication.Request,
    ): Authentication.Response = authenticationService.authenticate(request)
}
