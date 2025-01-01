package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
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
    meterRegistry: MeterRegistry,
    private val webClient: WebClient,
    private val property: AccountRegistryProperty,
) {

    private val accountRegistrySuccess = Counter.builder("account.registry.success")
        .description("Number of successful account registry calls")
        .register(meterRegistry)

    private val accountRegistryFailure = Counter.builder("account.registry.failure")
        .description("Number of failed account registry calls")
        .register(meterRegistry)

    suspend fun authenticate(
        request: Authentication.Request,
    ): Authentication.Response = try {
        webClient.accountRegistryAuthenticate(request, property)
            .also { accountRegistrySuccess.increment() }
    } catch (e: ErrorResponseException) {
        accountRegistryFailure.increment()
        throw e
    } catch (e: WebClientRequestException) {
        accountRegistryFailure.increment()
        log.error(e) { "Error device registry" }
        throw ResponseStatusException(SERVICE_UNAVAILABLE)
    } catch (e: WebClientResponseException) {
        accountRegistryFailure.increment()
        log.error(e) { "Error device registry" }
        throw ResponseStatusException(INTERNAL_SERVER_ERROR)
    }
}
