package ltd.hlaeja

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

fun logCall(url: String) {
    log.debug("calling: {}", url)
}
