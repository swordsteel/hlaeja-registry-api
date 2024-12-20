package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import ltd.hlaeja.library.deviceRegistry.Device
import ltd.hlaeja.property.DeviceRegistryProperty
import ltd.hlaeja.util.deviceRegistryCreateDevice
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import org.springframework.stereotype.Service
import org.springframework.web.ErrorResponseException
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class DeviceRegistryService(
    meterRegistry: MeterRegistry,
    private val webClient: WebClient,
    private val deviceRegistryProperty: DeviceRegistryProperty,
) {

    private val registerDeviceSuccess = Counter.builder("device.registry.success")
        .description("Number of successful device registrations")
        .register(meterRegistry)

    private val registerDeviceFailure = Counter.builder("device.registry.failure")
        .description("Number of failed device registrations")
        .register(meterRegistry)

    suspend fun registerDevice(
        request: Device.Request,
    ): Device.Response = try {
        webClient.deviceRegistryCreateDevice(request, deviceRegistryProperty)
            .also { registerDeviceSuccess.increment() }
    } catch (e: ErrorResponseException) {
        registerDeviceFailure.increment()
        throw e
    } catch (e: WebClientRequestException) {
        registerDeviceFailure.increment()
        log.error(e) { "Error device registry" }
        throw ResponseStatusException(SERVICE_UNAVAILABLE)
    }
}
