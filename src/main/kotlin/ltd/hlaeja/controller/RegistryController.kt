package ltd.hlaeja.controller

import ltd.hlaeja.library.deviceRegistry.Device
import ltd.hlaeja.service.DeviceRegistryService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RegistryController(
    private val registryService: DeviceRegistryService,
) {

    @PostMapping("/register")
    suspend fun addDevice(
        @RequestBody request: Device.Request,
    ): Device.Response = registryService.registerDevice(request)
}
