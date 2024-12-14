package ltd.hlaeja.service

import ltd.hlaeja.library.deviceRegistry.Device
import ltd.hlaeja.util.logCall
import ltd.hlaeja.property.DeviceRegistryProperty
import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.server.ResponseStatusException

@Service
class DeviceRegistryService(
    private val webClient: WebClient,
    private val deviceRegistryProperty: DeviceRegistryProperty,
) {
    suspend fun registerDevice(
        request: Device.Request,
    ): Device.Response = webClient.post()
        .uri("${deviceRegistryProperty.url}/device".also(::logCall))
        .bodyValue(request)
        .retrieve()
        .awaitBodyOrNull<Device.Response>() ?: throw ResponseStatusException(REQUEST_TIMEOUT)
}
