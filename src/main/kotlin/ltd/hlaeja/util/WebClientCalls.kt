package ltd.hlaeja.util

import ltd.hlaeja.library.accountRegistry.Authentication
import ltd.hlaeja.library.deviceRegistry.Device
import ltd.hlaeja.property.AccountRegistryProperty
import ltd.hlaeja.property.DeviceRegistryProperty
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.LOCKED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.http.HttpStatus.UNAUTHORIZED
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
    .onStatus(BAD_REQUEST::equals) { throw ResponseStatusException(BAD_REQUEST) }
    .awaitBodyOrNull<Device.Response>() ?: throw ResponseStatusException(REQUEST_TIMEOUT)

suspend fun WebClient.accountRegistryAuthenticate(
    request: Authentication.Request,
    property: AccountRegistryProperty,
): Authentication.Response = post()
    .uri("${property.url}/authenticate".also(::logCall))
    .bodyValue(request)
    .retrieve()
    .onStatus(LOCKED::equals) { throw ResponseStatusException(UNAUTHORIZED) }
    .onStatus(UNAUTHORIZED::equals) { throw ResponseStatusException(UNAUTHORIZED) }
    .onStatus(NOT_FOUND::equals) { throw ResponseStatusException(NOT_FOUND) }
    .awaitBodyOrNull<Authentication.Response>() ?: throw ResponseStatusException(REQUEST_TIMEOUT)
