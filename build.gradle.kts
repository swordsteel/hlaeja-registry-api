plugins {
    alias(hlaeja.plugins.kotlin.jvm)
    alias(hlaeja.plugins.kotlin.spring)
    alias(hlaeja.plugins.ltd.hlaeja.plugin.certificate)
    alias(hlaeja.plugins.ltd.hlaeja.plugin.service)
    alias(hlaeja.plugins.spring.dependency.management)
    alias(hlaeja.plugins.springframework.boot)
}

dependencies {
    implementation(hlaeja.fasterxml.jackson.module.kotlin)
    implementation(hlaeja.kotlin.logging)
    implementation(hlaeja.kotlin.reflect)
    implementation(hlaeja.kotlinx.coroutines)
    implementation(hlaeja.library.hlaeja.common.messages)
    implementation(hlaeja.springboot.starter.actuator)
    implementation(hlaeja.springboot.starter.webflux)

    testImplementation(hlaeja.mockk)
    testImplementation(hlaeja.projectreactor.reactor.test)
    testImplementation(hlaeja.kotlin.test.junit5)
    testImplementation(hlaeja.kotlinx.coroutines.test)
    testImplementation(hlaeja.springboot.starter.test)

    testRuntimeOnly(hlaeja.junit.platform.launcher)
}

group = "ltd.hlaeja"

tasks.named("processResources") {
    dependsOn("copyCertificates")
}
