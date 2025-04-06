package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import ltd.hlaeja.library.accountRegistry.Authentication
import ltd.hlaeja.property.AccountRegistryProperty
import ltd.hlaeja.util.accountRegistryAuthenticate
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import org.springframework.stereotype.Service
import org.springframework.web.ErrorResponseException
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class AuthenticationService(
    private val webClient: WebClient,
    private val property: AccountRegistryProperty,
) {

    suspend fun authenticate(
        request: Authentication.Request,
    ): Authentication.Response = try {
        webClient.accountRegistryAuthenticate(request, property)
    } catch (e: ErrorResponseException) {
        throw e
    } catch (e: WebClientRequestException) {
        log.error(e) { "Error device registry" }
        throw ResponseStatusException(SERVICE_UNAVAILABLE)
    } catch (e: WebClientResponseException) {
        log.error(e) { "Error device registry" }
        throw ResponseStatusException(INTERNAL_SERVER_ERROR)
    }
}
