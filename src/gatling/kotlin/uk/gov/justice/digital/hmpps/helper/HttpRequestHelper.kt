package uk.gov.justice.digital.hmpps.helper

import io.gatling.javaapi.http.AddCookie
import io.gatling.javaapi.http.HttpDsl
import uk.gov.justice.digital.hmpps.config.AuthConfig
import uk.gov.justice.digital.hmpps.config.HttpRequestConfig

class HttpRequestHelper(
    authConfig: AuthConfig = AuthConfig(),
    httpRequestConfig: HttpRequestConfig = HttpRequestConfig(),
) {
    var acpAuthCookie: AddCookie? = null

    init {
        if (authConfig.acpCookie != null) {
            acpAuthCookie =
                HttpDsl
                    .Cookie("hmpps-accredited-programmes-manage-and-deliver-ui.session", authConfig.acpCookie)
                    .withDomain(httpRequestConfig.domain)
                    .withPath("/")
                    .withSecure(true)
        }
    }
}
