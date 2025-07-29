plugins {
    alias(hlaeja.plugins.kotlin.jvm)
    alias(hlaeja.plugins.kotlin.spring)
    alias(hlaeja.plugins.spring.boot)
    alias(hlaeja.plugins.spring.dependency.management)
    alias(hlaeja.plugins.certificate)
    alias(hlaeja.plugins.service)
}

dependencies {
    implementation(hlaeja.fasterxml.jackson.module.kotlin)
    implementation(hlaeja.jjwt.api)
    implementation(hlaeja.kotlin.logging)
    implementation(hlaeja.kotlin.reflect)
    implementation(hlaeja.kotlinx.coroutines)
    implementation(hlaeja.library.common.messages)
    implementation(hlaeja.library.jwt)
    implementation(hlaeja.springboot.starter.actuator)
    implementation(hlaeja.springboot.starter.security)
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
