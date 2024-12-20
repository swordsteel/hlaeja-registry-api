package ltd.hlaeja.util

import ltd.hlaeja.library.deviceRegistry.Device
import ltd.hlaeja.property.DeviceRegistryProperty
import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.server.ResponseStatusException

suspend fun WebClient.deviceRegistryCreateDevice(
    request: Device.Request,
    property: DeviceRegistryProperty,
): Device.Response = post()
    .uri("${property.url}/device".also(::logCall))
    .bodyValue(request)
    .retrieve()
    .awaitBodyOrNull<Device.Response>() ?: throw ResponseStatusException(REQUEST_TIMEOUT)
