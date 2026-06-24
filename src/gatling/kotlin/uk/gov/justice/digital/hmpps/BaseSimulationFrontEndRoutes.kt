package uk.gov.justice.digital.hmpps

import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.config.HttpRequestConfig

open class BaseSimulationFrontEndRoutes(httpRequestConfig: HttpRequestConfig = HttpRequestConfig()) : Simulation() {
    protected val httpProtocol =
        HttpDsl.http.baseUrl("${httpRequestConfig.protocol}://${httpRequestConfig.domain}")
            .acceptHeader(httpRequestConfig.acceptHeader)
            .acceptLanguageHeader(httpRequestConfig.acceptLanguageHeader)
            .acceptEncodingHeader(httpRequestConfig.acceptEncodingHeader)
            .userAgentHeader(httpRequestConfig.userAgentHeader)

}